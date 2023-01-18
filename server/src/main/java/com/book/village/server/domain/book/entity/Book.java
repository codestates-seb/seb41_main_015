package com.book.village.server.domain.book.entity;

import com.book.village.server.domain.rate.entity.Rate;
import com.book.village.server.global.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column
    private Long totalRate=0L;

    @Column
    private Long rateCount=0L;

    @Column
    private Double avgRate=0.0;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book",orphanRemoval = true)
    private List<Rate> rates= new ArrayList<>();
}
