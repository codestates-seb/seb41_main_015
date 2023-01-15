package com.book.village.server.domain.borrowcomment.mapper;

import com.book.village.server.domain.borrowcomment.dto.BorrowCommentDto;
import com.book.village.server.domain.borrowcomment.entity.BorrowComment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BorrowCommentMapper {
    BorrowComment borrowCommentPostDtoToBorrowComment(BorrowCommentDto.Post borrowCommentPostDto);
    BorrowComment borrowCommentPatchDtoToBorrowComment(BorrowCommentDto.Patch borrowCommentPatchDto);
    BorrowCommentDto.Response borrowCommentToBorrowCommentResponseDto(BorrowComment borrowComment);
    List<BorrowCommentDto.Response> borrowContentsToBorrowCommentResponseDtos(List<BorrowComment> borrowComments);
}
