package com.book.village.server.domain.community_comment.repository;

import com.book.village.server.domain.community_comment.entity.CommunityComment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    List<CommunityComment> findAllByMember_Email(String email, Sort sort);
}
