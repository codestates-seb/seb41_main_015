package com.book.village.server.domain.request.repository;

import com.book.village.server.domain.request.dto.RequestDto;
import com.book.village.server.domain.request.entity.QRequest;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RequestQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public RequestQuerydslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<RequestDto.rankResponse> RequestRankByBookTitleCount() {
        QRequest request= QRequest.request;
        return queryFactory
                .select(Projections.fields(RequestDto.rankResponse.class,
                        request.bookTitle,
                        request.author,
                        request.publisher,
                        request.thumbnail,
                        request.bookTitle.count().as("count")))
                .from(request)
                .groupBy(request.bookTitle, request.author,request.publisher,request.thumbnail)
                .orderBy(request.bookTitle.count().desc())
                .limit(5)
                .fetch();

    }
}
