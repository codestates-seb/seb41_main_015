package com.book.village.server.domain.request.entity;

import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.request_comment.entity.RequestComment;
import com.book.village.server.global.audit.Auditable;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column
    @Lob
    private String thumbnail="https://dimg.donga.com/wps/NEWS/IMAGE/2011/11/17/41939226.1.jpg";

    @Column(length = 100)
    private String displayName;

    @Column
    private Long view = 0L;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "request", orphanRemoval = true)
    private List<RequestComment> requestComments =new ArrayList<>();

}
