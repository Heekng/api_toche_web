package com.heekng.api_toche_web.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnitTraitRepositoryImpl implements UnitTraitRepositoryCustom {

    private final JPAQueryFactory queryFactory;


}
