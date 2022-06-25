package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.MatchDetailDTO;
import com.heekng.api_toche_web.batch.dto.ParticipantDTO;
import com.heekng.api_toche_web.batch.dto.TraitDTO;
import com.heekng.api_toche_web.batch.dto.UnitDTO;
import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.repository.ItemRepository;
import com.heekng.api_toche_web.repository.TraitRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
import com.heekng.api_toche_web.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiMatchDetailProcessor implements ItemProcessor<TftMatch, List<MatchInfo>> {

    @Value("${riotApi.key}")
    private String RIOTAPI_KEY;
    @Value("${riotApi.path.matcheDetail}")
    private String RIOTAPI_PATH_MATCHDETAIL;

    private final TraitRepository traitRepository;
    private final SeasonService seasonService;
    private final AugmentService augmentService;
    private final ItemRepository itemRepository;
    private final UnitRepository unitRepository;

    @Override
    public List<MatchInfo> process(TftMatch tftMatch) throws Exception {
        String matchId = tftMatch.getMatchId();

        String uri = new StringBuilder()
                .append(RIOTAPI_PATH_MATCHDETAIL)
                .append(matchId)
                .append("?")
                .append("api_key=")
                .append(RIOTAPI_KEY)
                .toString();

        RestTemplate restTemplate = new RestTemplate();
        MatchDetailDTO matchDetailDTO = null;
        int apiRequestCount = 0;

        while (true) {
            try {
                apiRequestCount++;
                matchDetailDTO = restTemplate.getForObject(uri, MatchDetailDTO.class);
                break;
            } catch (HttpClientErrorException e) {
                String message = e.getMessage();
                if (apiRequestCount == 15) {
                    throw new RuntimeException("최대 요청 횟수 초과");
                } else if (message.contains("Rate limit exceeded")) {
                    log.info("wait maximum request");
                    Thread.sleep(120000);
                }
            }
        }

        // 이후 다시 조회하지 않기 위해 게임타입 업데이트
        tftMatch.updateGameType(matchDetailDTO.getInfo().getTft_game_type());

        // 게임타입이 standard 가 아닌 경우 저장하지 않는다.
        if (!matchDetailDTO.getInfo().getTft_game_type().equals("standard")) {
            log.info("matchId: {} is not standard", matchId);
            return null;
        }

        Integer tftSetNumber = matchDetailDTO.getInfo().getTft_set_number();
        String tftSetCoreName = matchDetailDTO.getInfo().getTft_set_core_name() != null ? matchDetailDTO.getInfo().getTft_set_core_name() : matchDetailDTO.getInfo().getTft_set_name();
        log.info("matchId: {}", matchId);
        if (tftSetCoreName == null) {
            tftSetCoreName = "TFTSet" + tftSetNumber;
        }

        Season season = seasonService.findOrSave(tftSetNumber, tftSetCoreName);
        LocalDateTime gameDatetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(matchDetailDTO.getInfo().getGame_datetime()), ZoneId.systemDefault());

        List<MatchInfo> matchInfos = new ArrayList<>();
        List<ParticipantDTO> winnerParticipantDtoList = matchDetailDTO.toWinParticipantList();
        for (int i = 0; i < winnerParticipantDtoList.size(); i++) {
            ParticipantDTO participantDTO = winnerParticipantDtoList.get(i);

            // matchInfo default entity
            MatchInfo matchInfo = MatchInfo.builder()
                    .season(season)
                    .gameDatetime(gameDatetime)
                    .tftMatch(tftMatch)
                    .ranking(i + 1)
                    .build();

            // augment
            List<String> augmentStringList = participantDTO.getAugments() != null ? participantDTO.getAugments() : new ArrayList<>();
            List<Augment> augments = augmentStringList.stream()
                    .map(augmentName -> augmentService.findOrSave(augmentName, makeAugmentOriginName(augmentName)))
                    .collect(Collectors.toList());
            for (Augment augment : augments) {
                MatchAugment matchAugment = MatchAugment.builder()
                        .augment(augment)
                        .build();
                matchInfo.addMatchAugment(matchAugment);
            }

            // unit & item
            List<UnitDTO> unitDTOS = participantDTO.getUnits();
            Map<String, Unit> unitMap = getExistUnitMap(season, unitDTOS);
            for (UnitDTO unitDTO : unitDTOS) {
                log.info(unitDTO.toString());
                Unit unit = unitMap.get(unitDTO.getCharacter_id());
                if (unit == null) {
                    unit = makeUnitAndSave(season, unitDTO);
                }
                List<Item> items = new ArrayList<>();
                if (!unitDTO.getItems().isEmpty()) {
                    Map<Integer, Item> itemMap = getExistItemMap(unitDTO.getItems());
                    for (int j = 0; j < unitDTO.getItems().size(); j++) {
                        Integer itemNum = unitDTO.getItems().get(j);
                        String itemName = unitDTO.getItemNames() != null ? unitDTO.getItemNames().get(j) : "";
                        Item item = itemMap.get(itemNum);
                        if (item == null) {
                            item = makeItemAndSave(itemNum, itemName);
                        }
                        if (!itemName.equals("") && !item.getName().equals(itemName)) {
                            item.updateName(itemName);
                        }
                        items.add(item);
                    }
                }
                MatchUnit matchUnit = MatchUnit.builder()
                        .unit(unit)
                        .build();
                for (Item item : items) {
                    MatchItem matchItem = MatchItem.builder()
                            .item(item)
                            .build();
                    matchUnit.addMatchItem(matchItem);
                }
                matchInfo.addMatchUnit(matchUnit);
            }

            // trait
            List<TraitDTO> traitDTOS = participantDTO.getTraits();
            Map<String, Trait> traitMap = getExistTraitMap(season, traitDTOS);
            for (TraitDTO traitDTO : traitDTOS) {
                Trait trait = traitMap.get(traitDTO.getName());
                if (trait == null) {
                    trait = makeTraitAndSave(season, traitDTO);
                }
                MatchTrait matchTrait = MatchTrait.builder()
                        .unitCount(traitDTO.getNum_units())
                        .tierAppliedCount(traitDTO.getTier_current())
                        .styleNum(traitDTO.getStyle())
                        .trait(trait)
                        .build();
                matchInfo.addMatchTrait(matchTrait);
            }
            matchInfos.add(matchInfo);
        }
        return matchInfos;
    }

    private Trait makeTraitAndSave(Season season, TraitDTO traitDTO) {
        Trait trait;
        trait = Trait.builder()
                .name(traitDTO.getName())
                .tierTotalCount(traitDTO.getTier_total())
                .season(season)
                .build();
        traitRepository.save(trait);
        return trait;
    }

    private Item makeItemAndSave(Integer itemNum, String itemName) {
        Item item;
        item = Item.builder()
                .name(itemName)
                .num(itemNum)
                .build();
        itemRepository.save(item);
        return item;
    }

    private Unit makeUnitAndSave(Season season, UnitDTO unitDTO) {
        Unit unit = Unit.builder()
                .name(unitDTO.getCharacter_id())
                .rarity(unitDTO.getRarity())
                .season(season)
                .build();
        unitRepository.save(unit);
        return unit;
    }

    private Map<Integer, Item> getExistItemMap(List<Integer> itemNums) {
        List<Item> itemList = itemRepository.searchByNums(itemNums);
        if (itemList.isEmpty()) {
            return new HashMap<>();
        }
        return itemList.stream().collect(Collectors.toMap(Item::getNum, item -> item));
    }

    private Map<String, Unit> getExistUnitMap(Season season, List<UnitDTO> unitDTOS) {
        List<String> characterIds = unitDTOS.stream().map(UnitDTO::getCharacter_id).collect(Collectors.toList());
        List<Unit> units = unitRepository.searchByNamesAndSeasonId(characterIds, season.getId());
        if (units.isEmpty()) {
            return new HashMap<>();
        }
        return units.stream().collect(Collectors.toMap(Unit::getName, unit -> unit));
    }

    private Map<String, Trait> getExistTraitMap(Season season, List<TraitDTO> traitDTOS) {
        List<String> traitNames = traitDTOS.stream().map(TraitDTO::getName).collect(Collectors.toList());
        List<Trait> traits = traitRepository.searchByNamesAndSeasonId(traitNames, season.getId());
        if (traits.isEmpty()) {
            return new HashMap<>();
        }
        return traits.stream().collect(Collectors.toMap(Trait::getName, trait -> trait));
    }

    private String makeAugmentOriginName(String name) {
        String[] nameSplit = name.split("_");
        char[] argumentOriginNameCharArray = nameSplit[nameSplit.length - 1].toCharArray();
        StringBuilder argumentNameBuilder = new StringBuilder();
        for (int i = 0; i < argumentOriginNameCharArray.length; i++) {
            char argumentOriginNameChar = argumentOriginNameCharArray[i];
            if (i != 0 && Character.isUpperCase(argumentOriginNameChar)) {
                argumentNameBuilder.append(" ");
                argumentNameBuilder.append(argumentOriginNameChar);
            } else if (i == argumentOriginNameCharArray.length - 1 && Character.isDigit(argumentOriginNameChar)) {
                argumentNameBuilder.append(" ");
                for (int j = 0; j < Integer.parseInt(String.valueOf(argumentOriginNameChar)); j++) {
                    argumentNameBuilder.append("I");
                }
                argumentNameBuilder.append(argumentOriginNameChar);
            } else {
                argumentNameBuilder.append(argumentOriginNameChar);
            }
        }
        return argumentNameBuilder.toString();
    }
}
