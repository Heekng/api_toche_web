package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.MatchDetailDTO;
import com.heekng.api_toche_web.batch.dto.ParticipantDTO;
import com.heekng.api_toche_web.batch.dto.TraitDTO;
import com.heekng.api_toche_web.batch.dto.UnitDTO;
import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.repository.ItemRepository;
import com.heekng.api_toche_web.repository.SeasonRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiMatchDetailProcessor implements ItemProcessor<TftMatch, List<MatchInfo>> {

    @Value("${riotApi.key}")
    private String RIOTAPI_KEY;
    @Value("${riotApi.path.matcheDetail}")
    private String RIOTAPI_PATH_MATCHDETAIL;

    private final SeasonRepository seasonRepository;
    private final SeasonService seasonService;
    private final AugmentService augmentService;
    private final UnitService unitService;
    private final ItemService itemService;
    private final TraitService traitService;
    private final ItemRepository itemRepository;

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

        // 게임타입이 standard 가 아닌 경우 저장하지 않는다.
        if (!matchDetailDTO.getInfo().getTft_game_type().equals("standard")) {
            return null;
        }

        Integer tftSetNumber = matchDetailDTO.getInfo().getTft_set_number();
        String tftSetCoreName = matchDetailDTO.getInfo().getTft_set_core_name() != null ? matchDetailDTO.getInfo().getTft_set_core_name() : matchDetailDTO.getInfo().getTft_set_name();

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
            List<String> augmentStringList = participantDTO.getAugments();
            List<Augment> augments = augmentStringList.stream().map(augmentName -> augmentService.findOrSave(augmentName)).collect(Collectors.toList());
            for (Augment augment : augments) {
                MatchAugment matchAugment = MatchAugment.builder()
                        .augment(augment)
                        .build();
                matchInfo.addMatchAugment(matchAugment);
            }

            // unit & item
            List<UnitDTO> unitDTOS = participantDTO.getUnits();
            for (UnitDTO unitDTO : unitDTOS) {
                log.info(unitDTO.toString());
                Unit unit = unitService.findOrSave(unitDTO.getCharacter_id(), unitDTO.getRarity(), season);
                List<Item> items = new ArrayList<>();
                for (int j = 0; j < unitDTO.getItems().size(); j++) {
                    Integer itemNum = unitDTO.getItems().get(j);
                    String itemName = "";
                    // 시즌 7부터 itemName이 제공되기 때문에 itemName이 없을 경우 처리
                    if (unitDTO.getItemNames() == null) {
                        Optional<Item> itemOptional = itemRepository.searchItemByRiotItemId(itemNum);
                        if (itemOptional.isPresent()) {
                            itemName = itemOptional.get().getName();
                        }
                    } else {
                        itemName = unitDTO.getItemNames().get(j);
                    }
                    Item item = itemService.findOrSave(itemName, itemNum, season);
                    items.add(item);
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
            for (TraitDTO traitDTO : traitDTOS) {
                Trait trait = traitService.findOrSave(traitDTO.getName(), traitDTO.getTier_total(), season);
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
}
