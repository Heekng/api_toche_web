package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.ChallengerInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengerInquiryRepository extends JpaRepository<ChallengerInquiry, Long>, ChallengerRepositoryCustom {
}
