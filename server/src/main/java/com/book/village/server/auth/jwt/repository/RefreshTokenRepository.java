package com.book.village.server.auth.jwt.repository;

import com.book.village.server.auth.jwt.entity.RefreshToken;
import com.book.village.server.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}
