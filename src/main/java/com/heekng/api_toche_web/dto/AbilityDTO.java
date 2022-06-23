package com.heekng.api_toche_web.dto;

import com.heekng.api_toche_web.entity.Ability;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AbilityDTO {

    @Data
    @NoArgsConstructor
    public static class AbilityResponse {

        private Long id;
        private String name;
        private String krName;
        private String abilityDesc;
        private String iconPath;

        public AbilityResponse(Ability ability) {
            this.id = ability.getId();
            this.name = ability.getName();
            this.krName = ability.getKrName();
            this.abilityDesc = ability.getAbilityDesc();
            this.iconPath = ability.getIconPath();
        }
    }

}
