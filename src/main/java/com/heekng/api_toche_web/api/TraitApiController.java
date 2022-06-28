package com.heekng.api_toche_web.api;

import com.heekng.api_toche_web.dto.TraitDTO;
import com.heekng.api_toche_web.entity.Trait;
import com.heekng.api_toche_web.repository.TraitRepository;
import com.heekng.api_toche_web.service.TraitService;
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
public class TraitApiController {

    private final TraitRepository traitRepository;
    private final ModelMapper standardMapper;
    private final TraitService traitService;

    @GetMapping("/traits")
    public List<TraitDTO.TraitsResponse> traits(
            @ModelAttribute TraitDTO.TraitsRequest traitsRequest
    ) {
        List<Trait> traits = traitRepository.searchByTraitsRequest(traitsRequest);
        return traits.stream()
                .map(trait -> standardMapper.map(trait, TraitDTO.TraitsResponse.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/traits/{traitId}")
    public TraitDTO.TraitDetailResponse traitByTraitId(
            @PathVariable(name = "traitId", required = true) Long traitId
    ) {
        return traitService.findTraitDetail(traitId);
    }
}
