package com.heekng.api_toche_web.batch.domain;

import com.heekng.api_toche_web.entity.Item;
import com.heekng.api_toche_web.entity.Season;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemInsertVO {

    private Integer id;
    private String name;
    private String description;
    private Boolean isUnique;
    private Boolean isElusive;
    private Boolean isRadiant;
    private String radiantBonus;

    @Builder
    public ItemInsertVO(Integer id, String name, String description, Boolean isUnique, Boolean isElusive, Boolean isRadiant, String radiantBonus) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isUnique = isUnique;
        this.isElusive = isElusive;
        this.isRadiant = isRadiant;
        this.radiantBonus = radiantBonus;
    }

    public Item toEntity() {
        return Item.builder()
                .name(this.name)
                .num(this.id)
                .build();
    }
}
