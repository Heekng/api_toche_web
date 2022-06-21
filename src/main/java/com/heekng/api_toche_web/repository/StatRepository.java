package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatRepository extends JpaRepository<Stat, Long>, StatRepositoryCustom {

}
