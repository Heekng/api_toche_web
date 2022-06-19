package com.heekng.api_toche_web.batch.dto.cDragon;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CDragonTftDTO {

    private List<CDragonItemDTO> items;
    private List<CDragonSetDataDTO> setData;
    private Map<String, CDragonSetDataDTO> sets;

}
