package com.heekng.api_toche_web.batch.dto.cDragon;

import com.heekng.api_toche_web.entity.Ability;
import lombok.Data;

import java.util.List;

@Data
public class CDragonAbilityDTO {

    private String desc;
    private String icon;
    private String name;
    private List<CDragonChampionAbilityVariableDTO> variables;

    private String enName;

    public Ability toAbilityEntity() {
        return Ability.builder()
                .iconPath(this.icon.toLowerCase().replace(".dds", ".png"))
                .desc(desc)
                .krName(name)
                .name(enName.equals("") ? null : enName)
                .build();
    }
}
