package com.book.village.server.domain.borrow.repository;

import com.book.village.server.domain.borrow.dto.BorrowDto;
import com.book.village.server.domain.borrow.entity.QBorrow;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class BorrowQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public BorrowQuerydslRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<BorrowDto.rankResponse> BorrowRankByBookTitleCount() {
        QBorrow request= QBorrow.borrow;
        return queryFactory
                .select(Projections.fields(BorrowDto.rankResponse.class,
                        request.bookTitle,
                        request.author,
                        request.publisher,
                        request.bookTitle.count().as("count")))
                .from(request)
                .groupBy(request.bookTitle, request.author,request.publisher)
                .orderBy(request.bookTitle.count().desc())
                .limit(5)
                .fetch();

    }
}
