package com.heekng.api_toche_web.repository;

import com.heekng.api_toche_web.entity.ChallengerInquiry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChallengerInquiryRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    ChallengerInquiryRepository challengerInquiryRepository;

    @Test
    void basicTest() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.of(2022, 6, 3, 21, 41);
        ChallengerInquiry challengerInquiry = ChallengerInquiry.builder()
                .inquiryDatetime(localDateTime)
                .build();

        // save
        challengerInquiryRepository.save(challengerInquiry);

        Optional<ChallengerInquiry> findByIdObject = challengerInquiryRepository.findById(challengerInquiry.getChallengerInquiryId());
        assertThat(findByIdObject).isNotEmpty();
        assertThat(findByIdObject.get()).isEqualTo(challengerInquiry);

        List<ChallengerInquiry> findAllObject = challengerInquiryRepository.findAll();
        assertThat(findAllObject).isNotEmpty();
        assertThat(findAllObject.size()).isEqualTo(1);

        challengerInquiryRepository.delete(challengerInquiry);
        Optional<ChallengerInquiry> afterDeleteObject = challengerInquiryRepository.findById(challengerInquiry.getChallengerInquiryId());
        assertThat(afterDeleteObject).isEmpty();
    }
}