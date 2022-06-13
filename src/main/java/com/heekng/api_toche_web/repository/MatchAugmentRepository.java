package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.MatchAugment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchAugmentRepository extends JpaRepository<MatchAugment, Long>, MatchAugmentRepositoryCustom {

}
