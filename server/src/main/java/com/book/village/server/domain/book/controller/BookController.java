package com.book.village.server.domain.book.controller;

import com.book.village.server.domain.book.dto.BookDto;
import com.book.village.server.domain.book.entity.Book;
import com.book.village.server.domain.book.mapper.BookMapper;
import com.book.village.server.domain.book.service.BookService;
import com.book.village.server.global.response.PageInfo;
import com.book.village.server.global.response.PageResponseDto;
import com.book.village.server.global.response.SingleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@Validated
@RequestMapping("/v1/books")
public class BookController {
    private final BookService bookService;
    private final BookMapper mapper;

    public BookController(BookService bookService, BookMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @PatchMapping("/{book-id}")
    public ResponseEntity patchBook(@PathVariable("book-id") long bookId,
                                    @Valid @RequestBody BookDto.Patch bookPatchDto,
                                    Principal principal){
        bookPatchDto.setBookId(bookId);
        Book book = bookService.updateBook(mapper.bookPatchDtoToBook(bookPatchDto),bookId);
        return ResponseEntity.ok(new SingleResponse<>(mapper.bookToBookResponseDto(book)));
    }

    @GetMapping("/{book-id}")
    public ResponseEntity getBook(@PathVariable("book-id") long bookId){
        Book book=bookService.findBook(bookId);
        return ResponseEntity.ok(new SingleResponse<>(mapper.bookToBookResponseDto(book)));
    }

    @GetMapping
    public ResponseEntity getBooks(@PageableDefault Pageable pageable){
        Page<Book> books = bookService.findBooks(pageable);
        return new ResponseEntity(
                new PageResponseDto<>(mapper.booksToBookResponseDtos(books.getContent()),
                        new PageInfo(books.getPageable(), books.getTotalElements())), HttpStatus.OK
        );
    }

    @GetMapping("/search")
    public ResponseEntity searchBooks(@RequestParam String keyword , @RequestParam String field, @PageableDefault Pageable pageable){
        Page<Book> books = bookService.searchBooks(keyword, field, pageable);
        return new ResponseEntity<>(new PageResponseDto<>(mapper.booksToBookResponseDtos(books.getContent()),
                new PageInfo(books.getPageable(), books.getTotalElements())),HttpStatus.OK);
    }

    @DeleteMapping("/{book-id}")
    public ResponseEntity deleteBooks(@PathVariable("book-id") long bookId){
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }
}
