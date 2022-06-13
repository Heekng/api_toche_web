package com.heekng.api_toche_web.batch.dto;

import com.heekng.api_toche_web.entity.MatchInfo;
import lombok.Data;

import java.util.List;

@Data
public class ParticipantDTO {

    private List<String> augments;
    private CompanionDTO companion;
    private Integer gold_left;
    private Integer last_round;
    private Integer level;
    private Integer placement;
    private Integer players_eliminated;
    private String puuid;
    private Float time_eliminated;
    private Integer total_damage_to_players;
    private List<TraitDTO> traits;
    private List<UnitDTO> units;

}
