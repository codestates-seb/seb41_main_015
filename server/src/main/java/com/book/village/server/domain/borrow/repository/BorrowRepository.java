package com.book.village.server.domain.borrow.repository;

import com.book.village.server.domain.borrow.entity.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    Page<Borrow> findAllByMember_Email(String email, Pageable pageable);
    Page<Borrow> findAll(Pageable pageable);

    Page<Borrow> findAllByTitleContaining(String keyword, Pageable pageable);

    Page<Borrow> findAllByContentContaining(String keyword, Pageable pageable);

    Page<Borrow> findAllByDisplayName(String keyword, Pageable pageable);

    Page<Borrow> findAllByBookTitleContaining(String keyword, Pageable pageable);

    Page<Borrow> findAllByAuthor(String keyword, Pageable pageable);
    Page<Borrow> findAllByPublisher(String keyword, Pageable pageable);

}
