package com.heekng.api_toche_web.batch.dto;

import lombok.Data;

import java.util.List;

@Data
public class MetadataDTO {

    private String data_version;
    private String match_id;
    private List<String> participants;

}
