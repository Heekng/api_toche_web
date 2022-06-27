package com.heekng.api_toche_web.batch.job.communityDragon;

import com.heekng.api_toche_web.batch.chunk.processor.*;
import com.heekng.api_toche_web.batch.chunk.writer.JpaItemListWriter;
import com.heekng.api_toche_web.batch.dto.cDragon.CDragonChampionDTO;
import com.heekng.api_toche_web.batch.dto.cDragon.CDragonItemDTO;
import com.heekng.api_toche_web.batch.dto.cDragon.CDragonSetDataDTO;
import com.heekng.api_toche_web.batch.dto.cDragon.CDragonTftDTO;
import com.heekng.api_toche_web.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CDragonAllDataPatchJobConfiguration {

    @Value("${cdragon.path.tftLastJson}")
    private String CDRAGON_PATH_TFTLASTJSON;
    @Value("${cdragon.path.tftLastJsonEn}")
    private String CDRAGON_PATH_TFTLASTJSONEN;

    private List<CDragonItemDTO> cDragonItemDTOFilterItemList;
    private List<CDragonItemDTO> cDragonItemDTOFilterAugmentList;
    private List<CDragonSetDataDTO> cDragonSetDataDTOList;
    private Integer itemIndex = 0;
    private Integer augmentIndex = 0;
    private Integer seasonIndex = 0;
    private Integer championIndex = 0;
    private Integer traitIndex = 0;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    private final CDragonItemInsertProcessor cDragonItemInsertProcessor;
    private final CDragonAugmentInsertProcessor cDragonAugmentInsertProcessor;
    private final CDragonSeasonInsertProcessor cDragonSeasonInsertProcessor;
    private final CDragonTraitInsertProcessor cDragonTraitInsertProcessor;
    private final CDragonChampionInsertProcessor cDragonChampionInsertProcessor;

    @Bean
    public Job cDragonAllDataPatchJob() {
        return jobBuilderFactory.get("cDragonAllDataPatchJob")
                .start(resetStep())
                .next(cDragonAllDataGetStep())
                .next(cDragonItemInsertStep())
                .next(cDragonAugmentInsertStep())
                .next(cDragonSeasonInsertStep())
                .next(cDragonTraitInsertStep())
                .next(cDragonChampionInsertStep())
                .build();
    }

    private Step resetStep() {
        return stepBuilderFactory.get("indexResetStep")
                .tasklet((contribution, chunkContext) -> {
                    itemIndex = 0;
                    augmentIndex = 0;
                    seasonIndex = 0;
                    championIndex = 0;
                    traitIndex = 0;
                    cDragonItemDTOFilterItemList = new ArrayList<>();
                    cDragonItemDTOFilterAugmentList = new ArrayList<>();
                    cDragonSetDataDTOList = new ArrayList<>();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private Step cDragonAllDataGetStep() {
        return stepBuilderFactory.get("cDragonAllDataGetStep")
                .tasklet((contribution, chunkContext) -> {
                    RestTemplate restTemplate = new RestTemplate();
                    CDragonTftDTO cDragonTftDTO = restTemplate.getForObject(CDRAGON_PATH_TFTLASTJSON, CDragonTftDTO.class);
                    //아이템
                    cDragonItemDTOFilterItemList = cDragonTftDTO.getItems().stream()
                            .filter(cDragonItemDTO -> !cDragonItemDTO.getIcon().contains("Augments"))
                            .filter(cDragonItemDTO -> !cDragonItemDTO.getIcon().contains("Mercenary"))
                            .collect(Collectors.toList());
                    //증강체
                    cDragonItemDTOFilterAugmentList = cDragonTftDTO.getItems().stream()
                            .filter(cDragonItemDTO -> cDragonItemDTO.getIcon().contains("Augments"))
                            .collect(Collectors.toList());
                    CDragonTftDTO enCDragonTftDto = restTemplate.getForObject(CDRAGON_PATH_TFTLASTJSONEN, CDragonTftDTO.class);
                    Map<Integer, String> enNameMap = enCDragonTftDto.getItems().stream()
                            .filter(cDragonItemDTO -> cDragonItemDTO.getIcon().contains("Augments"))
                            .filter(cDragonItemDTO -> cDragonItemDTO.getName() != null)
                            .collect(Collectors.toMap(CDragonItemDTO::getId, CDragonItemDTO::getName));
                    cDragonItemDTOFilterAugmentList.forEach(cDragonItemDTO ->
                            cDragonItemDTO.setNameEn(enNameMap.get(cDragonItemDTO.getId()))
                    );
                    //setData
                    Map<String, Map<String, String>> enAbilityNameMap = enCDragonTftDto.getSetData().stream()
                            .filter(cDragonSetDataDTO ->
                                    !cDragonSetDataDTO.getMutator().contains("PAIRS") && !cDragonSetDataDTO.getMutator().contains("TURBO") && !cDragonSetDataDTO.getMutator().contains("Tutorial")
                            )
                            .collect(Collectors.toMap(CDragonSetDataDTO::getMutator, setDataDTO ->
                                    setDataDTO.getChampions().stream()
                                            .collect(Collectors.toMap(CDragonChampionDTO::getApiName, championDTO ->
                                                championDTO.getAbility().getName() == null ? "" : championDTO.getAbility().getName()
                                            ))
                            ));

                    cDragonSetDataDTOList = cDragonTftDTO.getSetData().stream()
                            .filter(cDragonSetDataDTO ->
                                !cDragonSetDataDTO.getMutator().contains("PAIRS") && !cDragonSetDataDTO.getMutator().contains("TURBO")
                            )
                            .collect(Collectors.toList());
                    cDragonSetDataDTOList.forEach(cDragonSetDataDTO ->
                            cDragonSetDataDTO.getChampions().forEach(championDTO -> {
                                String enAbilityName = enAbilityNameMap.get(cDragonSetDataDTO.getMutator()).get(championDTO.getApiName());
                                championDTO.getAbility().setEnName(enAbilityName);
                            })
                    );

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private Step cDragonItemInsertStep() {
        return stepBuilderFactory.get("cDragonItemInsertStep")
                .<CDragonItemDTO, Item>chunk(10)
                .reader(cDragonItemInsertReader())
                .processor(cDragonItemInsertProcessor)
                .writer(cDragonItemInsertWriter())
                .build();
    }

    private ItemReader<CDragonItemDTO> cDragonItemInsertReader() {
        return new ItemReader<CDragonItemDTO>() {
            @Override
            public CDragonItemDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                CDragonItemDTO cDragonItemDTO = null;

                if (itemIndex < cDragonItemDTOFilterItemList.size()) {
                    cDragonItemDTO = cDragonItemDTOFilterItemList.get(itemIndex++);
                }

                return cDragonItemDTO;
            }
        };
    }

    private ItemWriter<Item> cDragonItemInsertWriter() {
        return new JpaItemWriterBuilder<Item>()
                .entityManagerFactory(emf)
                .usePersist(false)
                .build();
    }

    private Step cDragonAugmentInsertStep() {
        return stepBuilderFactory.get("cDragonAugmentInsertStep")
                .<CDragonItemDTO, Augment>chunk(10)
                .reader(cDragonAugmentInsertReader())
                .processor(cDragonAugmentInsertProcessor)
                .writer(cDragonAugmentInsertWriter())
                .build();
    }

    private ItemReader<CDragonItemDTO> cDragonAugmentInsertReader() {
        return new ItemReader<>() {
            @Override
            public CDragonItemDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                CDragonItemDTO cDragonItemDTO = null;

                if (augmentIndex < cDragonItemDTOFilterAugmentList.size()) {
                    cDragonItemDTO = cDragonItemDTOFilterAugmentList.get(augmentIndex++);
                }

                return cDragonItemDTO;
            }
        };
    }

    private ItemWriter<Augment> cDragonAugmentInsertWriter() {
        return new JpaItemWriterBuilder<Augment>()
                .entityManagerFactory(emf)
                .usePersist(false)
                .build();
    }

    private Step cDragonSeasonInsertStep() {
        return stepBuilderFactory.get("cDragonAugmentInsertStep")
                .<CDragonSetDataDTO, Season>chunk(10)
                .reader(cDragonSeasonInsertReader())
                .processor(cDragonSeasonInsertProcessor)
                .writer(cDragonSeasonInsertWriter())
                .build();
    }

    private ItemReader<CDragonSetDataDTO> cDragonSeasonInsertReader() {
        return new ItemReader<>() {
            @Override
            public CDragonSetDataDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                CDragonSetDataDTO cDragonSetDataDTO = null;

                if (seasonIndex < cDragonSetDataDTOList.size()) {
                    cDragonSetDataDTO = cDragonSetDataDTOList.get(seasonIndex++);
                }

                return cDragonSetDataDTO;
            }
        };
    }

    private ItemWriter<Season> cDragonSeasonInsertWriter() {
        return new JpaItemWriterBuilder<Season>()
                .entityManagerFactory(emf)
                .usePersist(false)
                .build();
    }

    private Step cDragonTraitInsertStep() {
        return stepBuilderFactory.get("cDragonTraitInsertStep")
                .<CDragonSetDataDTO, List<Trait>>chunk(10)
                .reader(cDragonTraitInsertReader())
                .processor(cDragonTraitInsertProcessor)
                .writer(cDragonTraitInsertWriter())
                .build();
    }

    private ItemReader<CDragonSetDataDTO> cDragonTraitInsertReader() {
        return new ItemReader<>() {
            @Override
            public CDragonSetDataDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                CDragonSetDataDTO cDragonSetDataDTO = null;

                if (traitIndex < cDragonSetDataDTOList.size()) {
                    cDragonSetDataDTO = cDragonSetDataDTOList.get(traitIndex++);
                }

                return cDragonSetDataDTO;
            }
        };
    }

    private ItemWriter<List<Trait>> cDragonTraitInsertWriter() {
        JpaItemWriter<Trait> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        writer.setUsePersist(false);

        return new JpaItemListWriter<>(writer);
    }

    private Step cDragonChampionInsertStep() {
        return stepBuilderFactory.get("cDragonChampionInsertStep")
                .<CDragonSetDataDTO, List<Unit>>chunk(10)
                .reader(cDragonChampionInsertReader())
                .processor(cDragonChampionInsertProcessor)
                .writer(cDragonChampionInsertWriter())
                .build();
    }

    private ItemReader<? extends CDragonSetDataDTO> cDragonChampionInsertReader() {
        return new ItemReader<>() {
            @Override
            public CDragonSetDataDTO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                CDragonSetDataDTO cDragonSetDataDTO = null;

                if (championIndex < cDragonSetDataDTOList.size()) {
                    cDragonSetDataDTO = cDragonSetDataDTOList.get(championIndex++);
                }

                return cDragonSetDataDTO;
            }
        };
    }

    private ItemWriter<List<Unit>> cDragonChampionInsertWriter() {
        JpaItemWriter<Unit> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        writer.setUsePersist(false);

        return new JpaItemListWriter<>(writer);
    }
}
