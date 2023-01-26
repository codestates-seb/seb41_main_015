package com.book.village.server.domain.borrowcomment.mapper;

import com.book.village.server.domain.borrowcomment.dto.BorrowCommentDto;
import com.book.village.server.domain.borrowcomment.entity.BorrowComment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowCommentMapper {
    BorrowComment borrowCommentPostDtoToBorrowComment(BorrowCommentDto.Post borrowCommentPostDto);
    BorrowComment borrowCommentPatchDtoToBorrowComment(BorrowCommentDto.Patch borrowCommentPatchDto);
    default BorrowCommentDto.Response borrowCommentToBorrowCommentResponseDto(BorrowComment borrowComment){
        if (borrowComment == null) {
            return null;
        } else {
            BorrowCommentDto.Response response = new BorrowCommentDto.Response();
            response.setBorrowCommentId(borrowComment.getBorrowCommentId());
            response.setContent(borrowComment.getContent());
            response.setDisplayName(borrowComment.getDisplayName());
            response.setImgUrl(borrowComment.getMember().getImgUrl());
            response.setCreatedAt(borrowComment.getCreatedAt());
            response.setModifiedAt(borrowComment.getModifiedAt());
            return response;
        }
    }
    List<BorrowCommentDto.Response> borrowCommentsToBorrowCommentResponseDtos(List<BorrowComment> borrowComments);
}
