package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.UseDeckAugment;
import com.heekng.api_toche_web.entity.UseDeckUnit;
import com.heekng.api_toche_web.entity.UseDeckUnitAugment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UseDeckUnitAugmentRepository extends JpaRepository<UseDeckUnitAugment, Long>, UseDeckUnitAugmentRepositoryCustom {

    Optional<UseDeckUnitAugment> findByUseDeckUnitAndUseDeckAugment(UseDeckUnit useDeckUnit, UseDeckAugment useDeckAugment);

    List<UseDeckUnitAugment> findByUseDeckAugment(UseDeckAugment useDeckAugment);
}
