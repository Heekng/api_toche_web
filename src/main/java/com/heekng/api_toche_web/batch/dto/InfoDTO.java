package com.heekng.api_toche_web.batch.dto;

import lombok.Data;

import java.util.List;

@Data
public class InfoDTO {

    private Long game_datetime;
    private Float game_length;
    private String game_version;
    private List<ParticipantDTO> participants;
    private Long queue_id;
    private String tft_game_type;
    private String tft_set_core_name;
    private String tft_set_name;
    private Integer tft_set_number;

}
