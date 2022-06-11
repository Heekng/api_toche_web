package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Summoner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SummonerRepository extends JpaRepository<Summoner, Long>, SummonerRepositoryCustom {
    Optional<Summoner> findByRiotSummonerId(String riotSummonerId);
}
