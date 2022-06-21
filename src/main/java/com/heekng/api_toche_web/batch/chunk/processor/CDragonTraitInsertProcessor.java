package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.cDragon.CDragonChampionDTO;
import com.heekng.api_toche_web.batch.dto.cDragon.CDragonSetDataDTO;
import com.heekng.api_toche_web.batch.dto.cDragon.CDragonTraitDTO;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.entity.Unit;
import com.heekng.api_toche_web.repository.SeasonRepository;
import com.heekng.api_toche_web.repository.TraitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CDragonTraitInsertProcessor implements ItemProcessor<CDragonSetDataDTO, List<Trait>> {

    @Value("${cdragon.path.image}")
    private String CDRAGON_PATH_IMAGE;
    private final SeasonRepository seasonRepository;
    private final TraitRepository traitRepository;

    @Override
    public List<Trait> process(CDragonSetDataDTO cDragonSetDataDTO) throws Exception {

        Season season = seasonRepository.findBySeasonName(cDragonSetDataDTO.getMutator())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시즌입니다."));

        List<Trait> traitList = new ArrayList<>();

        List<CDragonTraitDTO> traits = cDragonSetDataDTO.getTraits();

        for (CDragonTraitDTO traitDTO : traits) {
            Optional<Trait> traitOptional = traitRepository.findByNameAndSeasonId(traitDTO.getApiName(), season.getId());
            Trait trait = traitOptional.orElse(
                    Trait.builder()
                            .name(traitDTO.getApiName())
                            .season(season)
                            .tierTotalCount(traitDTO.getEffects().size())
                            .build()
            );
            String krName = traitDTO.getName();
            String iconPath = CDRAGON_PATH_IMAGE + traitDTO.getIcon().toLowerCase().replace(".tex", ".png");
            String desc = traitDTO.getDesc();
            trait.updateCDragonData(desc, iconPath, krName);
            traitList.add(trait);
        }

        return traitList;
    }
}
