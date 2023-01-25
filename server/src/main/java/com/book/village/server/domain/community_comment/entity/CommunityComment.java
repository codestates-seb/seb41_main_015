package com.book.village.server.domain.community_comment.entity;

import com.book.village.server.domain.community.entity.Community;
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
public class CommunityComment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityCommentId;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(length = 100)
    private String displayName;

    @ManyToOne
    @JoinColumn(name = "COMMUNITY_ID")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
