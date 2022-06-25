package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.dto.AugmentDTO;
import com.heekng.api_toche_web.entity.Augment;
import com.heekng.api_toche_web.entity.Season;
import com.heekng.api_toche_web.repository.AugmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AugmentService {

    private final AugmentRepository augmentRepository;

    @Transactional
    public Augment findOrSave(String name, String argumentOriginName) {
        Optional<Augment> augmentOptional = augmentRepository.searchByNameOrEnNameEq(name, argumentOriginName);
        Augment augment = null;
        if (augmentOptional.isEmpty()) {
            augment = Augment.builder()
                    .name(name)
                    .build();
            augmentRepository.save(augment);
        } else {
            augment = augmentOptional.get();
            if (!augment.getName().equals(name)) {
                augment.updateName(name);
            }
        }
        return augment;
    }

    public Boolean isExistAugments(List<Long> augments) {
        List<Optional<Augment>> augmentOptionalList = augments.stream()
                .map(augmentRepository::findById)
                .collect(Collectors.toList());
        for (Optional<Augment> augmentOptional: augmentOptionalList) {
            if (augmentOptional.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public List<Augment> findByArgumentsRequest(AugmentDTO.AugmentsRequest augmentsRequest) {
        return augmentsRequest.getSeasonId() == null ? augmentRepository.searchByAugmentsRequest(augmentsRequest) : augmentRepository.searchByAugmentsRequestContainsSeasonId(augmentsRequest);
    }
}
