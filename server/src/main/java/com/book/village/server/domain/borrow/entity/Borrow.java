package com.book.village.server.domain.borrow.entity;

import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.global.audit.Auditable;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Borrow extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowId;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(length = 100, nullable = false)
    private String bookTitle;

    @Column(length = 50, nullable = false)
    private String author;

    @Column(length = 50, nullable = false)
    private String publisher;

    @Column(length = 50, nullable = false)
    private String displayName;

    @Column(length = 100)
    private String talkUrl;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    @Setter
    private Member member;

}
