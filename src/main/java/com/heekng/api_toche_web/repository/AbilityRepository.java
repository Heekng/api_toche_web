package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Ability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbilityRepository extends JpaRepository<Ability, Long>, AbilityRepositoryCustom {

}
