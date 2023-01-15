package com.book.village.server.domain.borrowcomment.repository;

import com.book.village.server.domain.borrowcomment.entity.BorrowComment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowCommentRepository extends JpaRepository<BorrowComment, Long> {
    List<BorrowComment> findAllByMember_Email(String email, Sort sort);
}
