package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long>, UnitRepositoryCustom {

}
