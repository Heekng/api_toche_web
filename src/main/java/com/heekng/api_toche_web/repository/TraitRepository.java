package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Trait;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraitRepository extends JpaRepository<Trait, Long>, TraitRepositoryCustom {

}
