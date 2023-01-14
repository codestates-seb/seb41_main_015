package com.book.village.server.domain.community.entity;

import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.global.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Community extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityId;

    @Column(nullable = false)
    private String type;

    @Column(length = 100, nullable = false)
    private String title;


    @Column(nullable = false)
    @Lob
    private String content;

    @Column(length = 100)
    private String displayName;

    @ManyToOne
    @JoinColumn(name = "MEMEBER_ID")
    private Member member;
}
