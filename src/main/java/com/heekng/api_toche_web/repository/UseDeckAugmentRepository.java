package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.UseDeckAugment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseDeckAugmentRepository extends JpaRepository<UseDeckAugment, Long>, UseDeckAugmentRepositoryCustom {

}
