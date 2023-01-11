package com.book.village.server.domain.request.entity;

import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.global.audit.Auditable;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Request extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Column(length = 100)
    private String talkUrl;

    @Column(length = 100)
    private String title;

    @Lob
    private String content;

    @Column(length = 100)
    private String bookTitle;

    @Column(length = 100)
    private String author;

    @Column(length = 100)
    private String publisher;

    @Column(length = 100)
    private String displayName;

    @ManyToOne
    private Member member;

}
