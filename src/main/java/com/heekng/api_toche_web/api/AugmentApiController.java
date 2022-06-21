package com.heekng.api_toche_web.api;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.repository.AugmentRepository;
import com.heekng.api_toche_web.service.AugmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class AugmentApiController {

    private final AugmentRepository augmentRepository;
    private final AugmentService augmentService;
    private final ModelMapper standardMapper;

    @GetMapping("/augments")
    public List<AugmentDTO.AugmentsResponse> augments(
            @ModelAttribute AugmentDTO.AugmentsRequest augmentsRequest
    ) {
        List<Augment> augments = augmentService.findByArgumentsRequest(augmentsRequest);
        return augments.stream()
                .map(augment -> standardMapper.map(augment, AugmentDTO.AugmentsResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/augments/{augmentId}")
    public AugmentDTO.AugmentsResponse augmentByAugmentId(
            @PathVariable(name = "augmentId", required = true) Long augmentId
    ) {
        Augment augment = augmentRepository.findById(augmentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Trait 입니다."));
        return standardMapper.map(augment, AugmentDTO.AugmentsResponse.class);
    }
}
