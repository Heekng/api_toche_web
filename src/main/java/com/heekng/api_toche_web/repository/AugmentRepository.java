package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Augment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AugmentRepository extends JpaRepository<Augment, Long>, AugmentRepositoryCustom {

}
