package com.book.village.server.auth.jwt.entity;

import com.book.village.server.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @OneToOne
    @JoinColumn(name = "MEMBER")
    private Member member;

    private String refreshToken;

}
