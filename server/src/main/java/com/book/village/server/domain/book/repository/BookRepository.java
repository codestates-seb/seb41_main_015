package com.book.village.server.domain.book.repository;

import com.book.village.server.domain.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    Page<Book> findAll(Pageable pageable);

    Page<Book> findAllByIsbnContaining(String keyword, Pageable pageable);

    Page<Book> findAllByBookTitleContaining(String keyword, Pageable pageable);

    Page<Book> findAllByAuthorContaining(String keyword, Pageable pageable);

    Page<Book> findAllByPublisherContaining(String keyword, Pageable pageable);
}
