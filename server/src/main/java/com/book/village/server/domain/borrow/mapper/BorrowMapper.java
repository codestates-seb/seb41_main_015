package com.book.village.server.domain.borrow.mapper;

import com.book.village.server.domain.borrow.dto.BorrowDto;
import com.book.village.server.domain.borrow.entity.Borrow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowMapper {

    default Borrow borrowDtoPostToBorrow(BorrowDto.Post borrowDtoPost) {
        if (borrowDtoPost == null) {
            return null;
        } else {
            Borrow borrow = Borrow.builder()
                    .talkUrl(borrowDtoPost.getTalkUrl())
                    .title(borrowDtoPost.getTitle())
                    .content(borrowDtoPost.getContent())
                    .bookTitle(borrowDtoPost.getTitle())
                    .author(borrowDtoPost.getAuthor())
                    .publisher(borrowDtoPost.getPublisher())
                    .displayName(borrowDtoPost.getDisplayName())
                    .build();

            return borrow;
        }
    }


    Borrow borrowDtoPatchToBorrow(BorrowDto.Patch borrowDtoPatch);

    default BorrowDto.Response borrowToBorrowDtoResponse(Borrow borrow) {
        return BorrowDto.Response.builder()
                .borrowId(borrow.getBorrowId())
                .title(borrow.getTitle())
                .content(borrow.getContent())
                .bookTitle(borrow.getTitle())
                .author(borrow.getAuthor())
                .publisher(borrow.getPublisher())
                .displayName(borrow.getDisplayName())
                .talkUrl(borrow.getTalkUrl())
                .build();
    }

}
