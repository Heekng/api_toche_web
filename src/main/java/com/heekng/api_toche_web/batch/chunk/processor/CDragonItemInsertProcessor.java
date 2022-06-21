package com.heekng.api_toche_web.batch.chunk.processor;

import com.heekng.api_toche_web.batch.dto.cDragon.CDragonItemDTO;
import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CDragonItemInsertProcessor implements ItemProcessor<CDragonItemDTO, Item> {

    @Value("${cdragon.path.image}")
    private String CDRAGON_PATH_IMAGE;
    private final ItemRepository itemRepository;

    @Override
    public Item process(CDragonItemDTO cDragonItemDTO) throws Exception {

        Optional<Item> itemOptional = itemRepository.findByNum(cDragonItemDTO.getId());
        Item item = itemOptional.orElse(
                Item.builder()
                        .num(cDragonItemDTO.getId())
                        .name(cDragonItemDTO.getName())
                        .build()
        );

        String desc = cDragonItemDTO.getDesc();
        String korName = cDragonItemDTO.getName();
        Boolean isUnique = cDragonItemDTO.getUnique();
        String iconPath = CDRAGON_PATH_IMAGE + cDragonItemDTO.getIcon().toLowerCase().replace(".dds", ".png");
        Integer fromItem1 = null;
        Integer fromItem2 = null;
        if (!cDragonItemDTO.getFrom().isEmpty()) {
            fromItem1 = cDragonItemDTO.getFrom().get(0);
            fromItem2 = cDragonItemDTO.getFrom().get(1);
        }
        for (String key : cDragonItemDTO.getEffects().keySet()) {
            desc = desc.replace("@" + key + "@", String.valueOf(cDragonItemDTO.getEffects().get(key)));
        }
        log.info("desc: {}", desc);
        item.updateCDragonData(desc, korName, isUnique, iconPath, fromItem1, fromItem2);

        return item;
    }
}
