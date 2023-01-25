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

    @Column(length = 100)
    private String title;

    @Lob
    private String content;

    @Column(length = 100)
    private String bookTitle;

    @Column(length = 50)
    private String author;

    @Column(length = 50)
    private String publisher;

    @Column
    @Lob
    private String thumbnail="https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg";

    @Column(length = 50)
    private String displayName;

    @Column(length = 100)
    private String talkUrl;

    @Column
    private Long view = 0L;

    @Column
    private Boolean borrowWhthr = true;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    @Setter
    private Member member;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "borrow", orphanRemoval = true)
    private List<BorrowComment> borrowComments = new ArrayList<>();

}
