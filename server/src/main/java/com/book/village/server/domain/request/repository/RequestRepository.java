package com.book.village.server.domain.request.repository;

import com.book.village.server.domain.request.entity.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByMember_Email(String email,Pageable pageable);

    Page<Request> findAll(Pageable pageable);

    Page<Request> findAllByDisplayName(String displayName, Pageable pageable);

    Page<Request> findAllByTitleContaining(String title, Pageable pageable);

    Page<Request> findAllByContentContaining(String content, Pageable pageable);

    Page<Request> findAllByBookTitleContaining(String bookTitle, Pageable pageable);

    Page<Request> findAllByAuthor(String author, Pageable pageable);

    Page<Request> findAllByPublisher(String publisher, Pageable pageable);
}
