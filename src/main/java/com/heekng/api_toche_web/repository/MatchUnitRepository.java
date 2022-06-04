package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.MatchUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchUnitRepository extends JpaRepository<MatchUnit, Long>, MatchUnitRepositoryCustom {

}
