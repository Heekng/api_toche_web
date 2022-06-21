package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.cDragon.CDragonChampionDTO;
import com.heekng.api_toche_web.batch.dto.cDragon.CDragonSetDataDTO;
import com.heekng.api_toche_web.entity.*;
import com.heekng.api_toche_web.repository.SeasonRepository;
import com.heekng.api_toche_web.repository.TraitRepository;
import com.heekng.api_toche_web.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CDragonChampionInsertProcessor implements ItemProcessor<CDragonSetDataDTO, List<Unit>> {

    private final SeasonRepository seasonRepository;
    private final UnitRepository unitRepository;
    private final TraitRepository traitRepository;

    @Override
    public List<Unit> process(CDragonSetDataDTO cDragonSetDataDTO) throws Exception {

        Season season = seasonRepository.findBySeasonName(cDragonSetDataDTO.getMutator())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시즌입니다."));

        List<Unit> unitList = new ArrayList<>();

        List<CDragonChampionDTO> champions = cDragonSetDataDTO.getChampions();

        for (CDragonChampionDTO championDTO : champions) {
            Optional<Unit> unitOptional = unitRepository.findByNameAndSeasonId(championDTO.getApiName(), season.getId());
            Unit unit = unitOptional.orElse(
                    Unit.builder()
                            .name(championDTO.getApiName())
                            .season(season)
                            .build()
            );
            log.info("championDTO : {}", championDTO);
            String iconPath = championDTO.getIcon() != null ? championDTO.getIcon().toLowerCase().replace(".dds", ".png") : null;
            String krName = championDTO.getName();
            Integer cost = championDTO.getCost();
            unit.updateCDragonData(cost, iconPath, krName);

            // add stat
            Map<String, Stat> statMap = unit.getStats().stream()
                    .collect(Collectors.toMap(Stat::getName, stat -> stat));
            championDTO.getStats().forEach((name, statValue) -> {
                if (statMap.containsKey(name)) {
                    Stat stat = statMap.get(name);
                    stat.updateStat(name, statValue);
                } else {
                    Stat stat = Stat.builder()
                            .name(name)
                            .statValue(statValue)
                            .build();
                    unit.addStat(stat);
                }
            });

            // add trait
            championDTO.getTraits().forEach(traitName -> {
                Trait trait = traitRepository.findByKrNameAndSeasonId(traitName, season.getId())
                        .orElseThrow(() -> new IllegalStateException(traitName + " / " + season.getId() + " / 특성이 존재하지 않습니다."));
                UnitTrait unitTrait = UnitTrait.builder()
                        .trait(trait)
                        .build();
                unit.addUnitTrait(unitTrait);
            });

            Ability ability = championDTO.getAbility().toAbilityEntity();
            if (unitOptional.isEmpty() || unit.getAbilities().size() == 0) {
                // add ability
                unit.addAbility(ability);
            } else {
                Ability unitAbility = unit.getAbilities().get(0);
                unitAbility.updateByCDragonData(
                        ability.getDesc(),
                        ability.getIconPath(),
                        ability.getName(),
                        ability.getKrName()
                );
            }

            unitList.add(unit);
        }

        return unitList;
    }
}
