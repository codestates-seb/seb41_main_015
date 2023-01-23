package com.book.village.server.domain.borrowcomment.entity;

import com.book.village.server.domain.borrow.entity.Borrow;
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
public class BorrowComment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowCommentId;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(length = 50)
    private String displayName;

    @ManyToOne
    @JoinColumn(name = "BORROW_ID")
    private Borrow borrow;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

}
