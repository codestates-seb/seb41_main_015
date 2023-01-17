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

    Page<Borrow> findAllByWriteTitleContaining(String keyword, Pageable pageable);

    Page<Borrow> findAllByWriteContentContaining(String keyword, Pageable pageable);

    Page<Borrow> findAllByDisplayName(String keyword, Pageable pageable);


}
