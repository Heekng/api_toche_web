package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.MatchTrait;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchTraitRepository extends JpaRepository<MatchTrait, Long>, MatchTraitRepositoryCustom {

}
