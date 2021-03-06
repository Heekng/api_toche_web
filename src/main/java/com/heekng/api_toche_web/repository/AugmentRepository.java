package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.Augment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AugmentRepository extends JpaRepository<Augment, Long>, AugmentRepositoryCustom {

    Optional<Augment> findByName(String name);

    Optional<Augment> findByNum(Integer num);

    Optional<Augment> findByNameEndingWith(String name);
}
