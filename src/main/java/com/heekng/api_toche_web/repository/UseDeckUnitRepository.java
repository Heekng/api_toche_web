package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.UseDeckUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UseDeckUnitRepository extends JpaRepository<UseDeckUnit, Long>, UseDeckUnitRepositoryCustom {

}
