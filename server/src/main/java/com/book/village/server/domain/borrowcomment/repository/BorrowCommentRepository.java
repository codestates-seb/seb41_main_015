package com.book.village.server.domain.borrowcomment.repository;

import com.book.village.server.domain.borrowcomment.entity.BorrowComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowCommentRepository extends JpaRepository<BorrowComment, Long> {
}
