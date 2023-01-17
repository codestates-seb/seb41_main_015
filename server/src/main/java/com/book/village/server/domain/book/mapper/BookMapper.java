package com.book.village.server.domain.book.mapper;

import com.book.village.server.domain.book.dto.BookDto;
import com.book.village.server.domain.book.entity.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book bookPatchDtoToBook(BookDto.Patch bookPatchDto);
    BookDto.Response bookToBookResponseDto(BookDto.Response bookResponseDto);
    List<BookDto.Response> booksToBookResponseDtos(List<BookDto.Response> bookResponseDtos);
}
