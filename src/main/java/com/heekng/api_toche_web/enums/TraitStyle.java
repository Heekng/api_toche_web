package com.heekng.api_toche_web.enums;

import java.util.Arrays;

public enum TraitStyle {
    _0("noStyle"),
    _1("bronze"),
    _2("silver"),
    _3("gold"),
    _4("chromatic");

    private final String styleName;

    TraitStyle(String styleName) {
        this.styleName = styleName;
    }

    public static TraitStyle findByStyleName(String styleName) {
        return Arrays.stream(TraitStyle.values())
                .filter(traitStyle -> traitStyle.styleName.equals(styleName))
                .findAny()
                .orElse(null);
    }
}
