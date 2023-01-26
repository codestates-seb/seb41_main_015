package com.book.village.server.domain.borrow.mapper;

import com.book.village.server.domain.borrow.dto.BorrowDto;
import com.book.village.server.domain.borrow.entity.Borrow;
import com.book.village.server.domain.borrowcomment.mapper.BorrowCommentMapper;
import com.book.village.server.domain.borrowcomment.mapper.BorrowCommentMapperImpl;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BorrowMapper {
    BorrowCommentMapper borrowCommentMapper = new BorrowCommentMapperImpl();
    Borrow borrowDtoPostToBorrow(BorrowDto.Post borrowDtoPost);
    Borrow borrowDtoPatchToBorrow(BorrowDto.Patch borrowDtoPatch);

    default BorrowDto.Response borrowToBorrowDtoResponse(Borrow borrow) {
        if (borrow == null) {
            return null;
        }
        BorrowDto.Response response = new BorrowDto.Response();
        response.setBorrowId(borrow.getBorrowId());
        response.setTitle(borrow.getTitle());
        response.setContent(borrow.getContent());
        response.setBookTitle(borrow.getBookTitle());
        response.setAuthor(borrow.getAuthor());
        response.setPublisher(borrow.getPublisher());
        response.setThumbnail(borrow.getThumbnail());
        response.setDisplayName(borrow.getMember().getDisplayName());
        response.setImgUrl(borrow.getMember().getImgUrl());
        response.setTalkUrl(borrow.getTalkUrl());
        response.setBorrowWhthr(borrow.getBorrowWhthr());
        response.setView(borrow.getView());
        if (borrow.getBorrowComments() != null) {
            response.setBorrowComments(borrow.getBorrowComments().stream()
                    .map(borrowComment -> borrowCommentMapper.borrowCommentToBorrowCommentResponseDto(borrowComment))
                    .collect(Collectors.toList()));
        }
        response.setCreatedAt(borrow.getCreatedAt());
        response.setModifiedAt(borrow.getModifiedAt());

        return response;
    }

    default List<BorrowDto.Response> borrowsToBorrowResponseDtos(List<Borrow> borrows) {
        if(borrows == null) {
            return null;
        }
        List<BorrowDto.Response> list = new ArrayList<BorrowDto.Response>(  borrows.size() );
        for( Borrow borrow : borrows) {
            BorrowDto.Response response = borrowToBorrowDtoResponse( borrow );
            response.setBorrowComments(null);
            list.add(response);
        }
        return list;
    }
}
