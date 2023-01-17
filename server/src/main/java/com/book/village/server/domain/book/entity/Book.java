package com.book.village.server.domain.book.entity;

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
public class Book extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(length = 50, unique = true, nullable = false)
    private String isbn;

    @Column
    private String bookTitle;

    @Column
    private String author;

    @Column
    private String publisher;

}
