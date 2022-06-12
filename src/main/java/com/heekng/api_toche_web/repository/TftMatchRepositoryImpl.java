package com.heekng.api_toche_web.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TftMatchRepositoryImpl implements TftMatchRepositoryCustom {

    private final JPAQueryFactory queryFactory;


}
