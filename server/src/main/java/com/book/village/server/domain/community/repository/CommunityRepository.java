package com.book.village.server.domain.community.repository;

import com.book.village.server.domain.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findAllByMember_Email(String email, Pageable pageable);
    Page<Community> findAll(Pageable pageable);

    Page<Community> findAllByTitleContainingAndType(String keyword, String type, Pageable pageable);

    Page<Community> findAllByContentContainingAndType(String keyword,String type, Pageable pageable);

    Page<Community> findAllByDisplayNameAndType(String keyword, String type, Pageable pageable);
}
