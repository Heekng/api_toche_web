package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Challenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengerRepository extends JpaRepository<Challenger, Long>, ChallengerRepositoryCustom{

}
