package com.book.village.server.domain.borrow.repository;

import com.book.village.server.domain.borrow.entity.Borrow;
import com.book.village.server.domain.borrow.entity.BorrowRank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    @Query(value =
            "select book_title, author, publisher, count(book_title) as count " +
                    "from borrow group by book_title, author, publisher order by count desc limit 5;", nativeQuery = true)
    List<BorrowRank> findRankedBorrows();

    Page<Borrow> findAllByMember_Email(String email, Pageable pageable);
    Page<Borrow> findAll(Pageable pageable);

    Page<Borrow> findAllByTitleContaining(String keyword, Pageable pageable);

    Page<Borrow> findAllByContentContaining(String keyword, Pageable pageable);

    Page<Borrow> findAllByDisplayName(String keyword, Pageable pageable);

    Page<Borrow> findAllByBookTitleContaining(String keyword, Pageable pageable);

    Page<Borrow> findAllByAuthor(String keyword, Pageable pageable);
    Page<Borrow> findAllByPublisher(String keyword, Pageable pageable);

}
