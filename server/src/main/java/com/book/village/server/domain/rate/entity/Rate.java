package com.book.village.server.domain.rate.entity;

import com.book.village.server.domain.book.entity.Book;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.global.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rate extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rateId;

    @Column
    @Range(min = 1, max = 5)
    private Long rating;

    @Column
    private String displayName;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne
    @JoinColumn(name="BOOK_ID")
    private Book book;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;
}
