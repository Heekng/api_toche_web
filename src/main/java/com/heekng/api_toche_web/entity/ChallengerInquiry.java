package com.heekng.api_toche_web.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengerInquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "challenger_inquiry_id")
    private Long challengerInquiryId;
    @Column(name = "inquiry_datetime", nullable = false, unique = true)
    private LocalDateTime inquiryDatetime;

    @OneToMany(mappedBy = "challengerInquiry", cascade = REMOVE)
    private List<Challenger> challengers = new ArrayList<>();

    @Builder
    public ChallengerInquiry(LocalDateTime inquiryDatetime) {
        this.inquiryDatetime = inquiryDatetime;
    }

}
