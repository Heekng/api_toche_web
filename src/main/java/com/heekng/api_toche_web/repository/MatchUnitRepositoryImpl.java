package com.heekng.api_toche_web.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchUnitRepositoryImpl implements MatchUnitRepositoryCustom {

    private final JPAQueryFactory queryFactory;


}
