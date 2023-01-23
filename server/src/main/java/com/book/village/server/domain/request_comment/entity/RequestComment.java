package com.book.village.server.domain.request_comment.entity;

import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.global.audit.Auditable;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestComment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestCommentId;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(length = 100)
    private String displayName;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID")
    private Request request;
}
