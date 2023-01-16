package com.book.village.server.domain.request_comment.repository;

import com.book.village.server.domain.request_comment.entity.RequestComment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestCommentRepository extends JpaRepository<RequestComment, Long> {
    List<RequestComment> findAllByMember_Email(String email, Sort sort);
}

