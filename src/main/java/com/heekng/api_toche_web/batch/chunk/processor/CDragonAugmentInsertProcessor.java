package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.cDragon.CDragonItemDTO;
import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.repository.AugmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CDragonAugmentInsertProcessor implements ItemProcessor<CDragonItemDTO, Augment> {

    @Value("${cdragon.path.image}")
    private String CDRAGON_PATH_IMAGE;
    private final AugmentRepository augmentRepository;

    @Override
    public Augment process(CDragonItemDTO cDragonAugmentDTO) throws Exception {
        // 기존에 추가되어있는 증강체를 지우지 않기 위해 사용
        String nameEn = cDragonAugmentDTO.getNameEn();
        if (nameEn == null) {
            return null;
        }
        nameEn = nameEn.replace("'", "");
        String[] nameEnSplit = nameEn.split(" ");
        StringBuilder nameEnBuilder = new StringBuilder();
        for (int i = 0; i < nameEnSplit.length; i++) {
            if (i != nameEnSplit.length - 1) {
                nameEnBuilder.append(nameEnSplit[i]);
            } else {
                Boolean isAllI = true;
                String lastStr = nameEnSplit[i];
                for (char aChar : lastStr.toCharArray()) {
                    if (aChar != 'I') {
                        isAllI = false;
                        break;
                    }
                }
                nameEnBuilder.append(isAllI ? lastStr.length() : lastStr);
            }
        }
        String findNameEn = nameEnBuilder.toString();

        Optional<Augment> augmentOptional = augmentRepository.findByNum(cDragonAugmentDTO.getId());
        if (augmentOptional.isEmpty()) {
            augmentOptional = augmentRepository.findByNameEndingWith(findNameEn);
        }
        Augment augment = augmentOptional.orElse(
                Augment.builder()
                        .name(nameEn)
                        .build()
        );
        Integer num = cDragonAugmentDTO.getId();
        String desc = cDragonAugmentDTO.getDesc();
        for (String key : cDragonAugmentDTO.getEffects().keySet()) {
            desc = desc.replace("@" + key + "@", String.valueOf(cDragonAugmentDTO.getEffects().get(key)));
        }
        String krName = cDragonAugmentDTO.getName();
        Boolean isUnique = cDragonAugmentDTO.getUnique();
        String iconPath = CDRAGON_PATH_IMAGE + cDragonAugmentDTO.getIcon().toLowerCase().replace(".dds", ".png");
        augment.updateCDragonData(num, desc, krName, nameEn, isUnique, iconPath);

        return augment;
    }
}
