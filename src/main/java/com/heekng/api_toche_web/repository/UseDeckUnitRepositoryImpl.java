package com.heekng.api_toche_web.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UseDeckUnitRepositoryImpl implements UseDeckUnitRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}
