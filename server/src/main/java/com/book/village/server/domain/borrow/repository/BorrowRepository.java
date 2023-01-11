package com.book.village.server.domain.borrow.repository;

import com.book.village.server.domain.borrow.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

}
