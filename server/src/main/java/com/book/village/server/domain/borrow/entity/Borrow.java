package com.book.village.server.domain.borrow.entity;

import com.book.village.server.domain.borrowcomment.entity.BorrowComment;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.global.audit.Auditable;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private String borrowTitle;

    @Lob
    private String content;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String authors;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "borrow", orphanRemoval = true)
    private List<BorrowComment> borrowComments = new ArrayList<>();

}
