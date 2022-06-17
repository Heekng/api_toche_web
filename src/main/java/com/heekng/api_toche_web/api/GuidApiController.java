package com.heekng.api_toche_web.api;

import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.service.GuidService;
import com.heekng.api_toche_web.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class GuidApiController {

    private final GuidService guidService;
    private final UnitService unitService;

    @GetMapping("/guid/units")
    public UnitDTO.GuidResultResponse guidByUnits(
            @ModelAttribute UnitDTO.GuidRequest guidRequest
    ) {
        Boolean existUnits = unitService.isExistUnits(guidRequest.getUnitIds());
        if (!existUnits) throw new IllegalArgumentException("존재하지 않는 유닛 ID 입니다.");
        return guidService.guidByUnits(guidRequest);
    }
}
