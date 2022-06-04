package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.UnitTrait;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitTraitRepository extends JpaRepository<UnitTrait, Long>, UnitTraitRepositoryCustom {

}
