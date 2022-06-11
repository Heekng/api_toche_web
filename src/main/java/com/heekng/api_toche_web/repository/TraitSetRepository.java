package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.TraitSet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraitSetRepository extends JpaRepository<TraitSet, Long>, TraitSetRepositoryCustom {

}
