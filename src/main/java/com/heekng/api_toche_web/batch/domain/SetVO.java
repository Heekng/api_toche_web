package com.heekng.api_toche_web.batch.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SetVO {

    private String style;
    private Integer min;
    private Integer max;

    @Builder
    public SetVO(String style, Integer min, Integer max) {
        this.style = style;
        this.min = min;
        this.max = max;
    }

}
