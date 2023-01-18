package com.book.village.server.domain.borrow.mapper;

import com.book.village.server.domain.borrow.dto.BorrowDto;
import com.book.village.server.domain.borrow.entity.Borrow;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowMapper {

    default Borrow borrowDtoPostToBorrow(BorrowDto.Post borrowDtoPost) {
        if (borrowDtoPost == null) {
            return null;
        } else {
            Borrow borrow = Borrow.builder()
                    .talkUrl(borrowDtoPost.getTalkUrl())
                    .borrowTitle(borrowDtoPost.getBorrowTitle())
                    .content(borrowDtoPost.getContent())
                    .title(borrowDtoPost.getTitle())
                    .authors(borrowDtoPost.getAuthors())
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
                .borrowTitle(borrow.getBorrowTitle())
                .content(borrow.getContent())
                .title(borrow.getTitle())
                .authors(borrow.getAuthors())
                .publisher(borrow.getPublisher())
                .displayName(borrow.getDisplayName())
                .talkUrl(borrow.getTalkUrl())
                .createdAt(borrow.getCreatedAt())
                .modifiedAt(borrow.getModifiedAt())
                .build();
    }


    default List<BorrowDto.Response> borrowsToBorrowResponseDtos(List<Borrow> borrows) {
        if(borrows == null) {
            return null;
        }
        List<BorrowDto.Response> list = new ArrayList<BorrowDto.Response>(
                borrows.size() );

        for( Borrow borrow : borrows) {
            BorrowDto.Response response = borrowToBorrowDtoResponse( borrow );
            response.setBorrowComments(null);
            list.add(response);
        }
        return list;
    }
}
