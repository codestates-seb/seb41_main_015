package com.book.village.server.domain.book.service;

import com.book.village.server.domain.book.entity.Book;
import com.book.village.server.domain.book.repository.BookRepository;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class BookService {
    private final BookRepository repository;
    private final CustomBeanUtils<Book> beanUtils;

    public BookService(BookRepository repository, CustomBeanUtils<Book> beanUtils) {
        this.repository = repository;
        this.beanUtils = beanUtils;
    }

    public Book createBook(Book book){
        verifyExistsIsbn(book.getIsbn());
        return repository.save(book);
    }

    public Book updateBook(Book book, long bookId){
        Book findBook= findVerifiedBook(bookId);
        beanUtils.copyNonNullProperties(book, findBook);
        return repository.save(findBook);
    }
    public Book findBook(long bookId){
        return findVerifiedBook(bookId);
    }
    public Page<Book> findBooks(Pageable pageable){
        return repository.findAll(pageable);
    }
    public Page<Book> searchBooks(String keyword, String field, Pageable pageable){
        switch (field){
            case "isbn":
                return repository.findAllByIsbnContaining(keyword, pageable);
            case "bookTitle":
                return repository.findAllByBookTitleContaining(keyword, pageable);
            case "author":
                return repository.findAllByAuthorContaining(keyword, pageable);
            case "publisher":
                return repository.findAllByPublisherContaining(keyword, pageable);
            default:
                return new PageImpl<>(Collections.emptyList());
        }
    }

    public void deleteBook(long bookId){
        Book findBook=findVerifiedBook(bookId);
        repository.delete(findBook);
    }

    public Book findVerifiedBook(long bookId){
        Optional<Book> optionalBook=repository.findById(bookId);
        Book book =
                optionalBook.orElseThrow(()->
                        new CustomLogicException(ExceptionCode.BOOK_NOT_FOUND));
        return book;
    }

    public void verifyExistsIsbn(String isbn){
        Optional<Book> book = repository.findByIsbn(isbn);
        if(book.isPresent()) throw new CustomLogicException(ExceptionCode.BOOK_EXISTS);
    }
}
