package com.heekng.api_toche_web.service;

import com.heekng.api_toche_web.entity.UseDeckAugment;
import com.heekng.api_toche_web.entity.UseDeckUnit;
import com.heekng.api_toche_web.entity.UseDeckUnitAugment;
import com.heekng.api_toche_web.repository.UseDeckUnitAugmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UseDeckUnitAugmentService {

    private final UseDeckUnitAugmentRepository useDeckUnitAugmentRepository;

    @Transactional
    public UseDeckUnitAugment findOrSaveByUseDeckUnitAndUseDeckAugment(UseDeckUnit useDeckUnit, UseDeckAugment useDeckAugment) {
        Optional<UseDeckUnitAugment> useDeckUnitAugmentOptional = useDeckUnitAugmentRepository.findByUseDeckUnitAndUseDeckAugment(useDeckUnit, useDeckAugment);
        UseDeckUnitAugment useDeckUnitAugment;
        if (useDeckUnitAugmentOptional.isPresent()) {
            useDeckUnitAugment = useDeckUnitAugmentOptional.get();
        } else {
            useDeckUnitAugment = UseDeckUnitAugment.builder()
                    .useCount(0L)
                    .useDeckUnit(useDeckUnit)
                    .useDeckAugment(useDeckAugment)
                    .build();
            useDeckUnitAugmentRepository.save(useDeckUnitAugment);
        }
        return useDeckUnitAugment;
    }
}
