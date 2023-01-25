package com.book.village.server.domain.book.mapper;

import com.book.village.server.domain.book.dto.BookDto;
import com.book.village.server.domain.book.entity.Book;
import com.book.village.server.domain.rate.mapper.RateMapper;
import com.book.village.server.domain.rate.mapper.RateMapperImpl;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {
    RateMapper rateMapper = new RateMapperImpl();
    Book bookPatchDtoToBook(BookDto.Patch bookPatchDto);
    default BookDto.Response bookToBookResponseDto(Book book){
        if (book == null) {
            return null;
        } else {
            BookDto.Response response = new BookDto.Response();
            response.setBookId(book.getBookId());
            response.setIsbn(book.getIsbn());
            response.setBookTitle(book.getBookTitle());
            response.setAuthor(book.getAuthor());
            response.setPublisher(book.getPublisher());
            response.setThumbnail(book.getThumbnail());
            response.setAvgRate(book.getAvgRate());
            response.setRates(book.getRates().stream()
                    .map(rate-> rateMapper.rateToRateResponseDto(rate))
                    .collect(Collectors.toList()));
            response.setCreatedAt(book.getCreatedAt());
            response.setModifiedAt(book.getModifiedAt());
            return response;
        }
    }
    default List<BookDto.Response> booksToBookResponseDtos(List<Book> books){
        if (books == null) {
            return null;
        } else {
            List<BookDto.Response> list = new ArrayList(books.size());
            Iterator var3 = books.iterator();

            while(var3.hasNext()) {
                Book book = (Book)var3.next();
                BookDto.Response bookResponseDto = this.bookToBookResponseDto(book);
                bookResponseDto.setRates(null);
                list.add(bookResponseDto);
            }

            return list;
        }
    }
}
