package com.heekng.api_toche_web.api;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.dto.GuidDTO;
import com.heekng.api_toche_web.dto.UnitDTO;
import com.heekng.api_toche_web.service.AugmentService;
import com.heekng.api_toche_web.service.GuidService;
import com.heekng.api_toche_web.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/guid")
public class GuidApiController {

    private final GuidService guidService;
    private final UnitService unitService;
    private final AugmentService augmentService;

    @GetMapping("/units")
    public GuidDTO.GuidResultResponse guidByUnits(
            @Validated @ModelAttribute UnitDTO.GuidRequest guidRequest
    ) {
        Boolean existUnits = unitService.isExistUnits(guidRequest.getUnitIds());
        if (!existUnits) throw new IllegalArgumentException("존재하지 않는 유닛 ID 입니다.");
        return guidService.guidByUnits(guidRequest);
    }

    @GetMapping("/augment")
    public GuidDTO.GuidResultResponse guidByAugments(
            @Validated @ModelAttribute AugmentDTO.GuidRequest guidRequest
    ) {
        Boolean existAugments = augmentService.isExistAugments(guidRequest.getAugmentIds());
        if (!existAugments) throw new IllegalArgumentException("존재하지 않는 증강체 ID 입니다.");
        return guidService.guidByAugments(guidRequest);
    }
}
