package com.heekng.api_toche_web.batch.dto;

import com.heekng.api_toche_web.entity.MatchInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatchDetailDTO {

    private MetadataDTO metadata;
    private InfoDTO info;

    public List<ParticipantDTO> toWinParticipantList() {
        return new ArrayList<>(this.info.getParticipants().subList(0, 4));
    }

}
