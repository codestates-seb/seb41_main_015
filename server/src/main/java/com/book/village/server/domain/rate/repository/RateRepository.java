package com.book.village.server.domain.rate.repository;

import com.book.village.server.domain.rate.entity.Rate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate,Long> {
    Page<Rate> findAllByMember_Email(String email, Pageable pageable);

    Optional<Rate> findByMember_EmailAndBook_Isbn(String email, String isbn);
}
