package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.UseDeckUnitAugment;
import com.heekng.api_toche_web.entity.UseUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseDeckUnitAugmentRepository extends JpaRepository<UseDeckUnitAugment, Long>, UseDeckUnitAugmentRepositoryCustom {

}
