package com.book.village.server.domain.request_comment.mapper;

import com.book.village.server.domain.request_comment.dto.RequestCommentDto;
import com.book.village.server.domain.request_comment.entity.RequestComment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestCommentMapper {

    RequestComment requestCommentPostDtoToRequestComment(RequestCommentDto.Post requestCommentPostDto);

    RequestComment requestCommentPatchDtoToRequestComment(RequestCommentDto.Patch requestCommentPatchDto);

    RequestCommentDto.Response requestCommentToRequestCommentResponseDto(RequestComment requestComment);

    List<RequestCommentDto.Response> requestCommentsToRequestCommentResponseDtos(List<RequestComment> requestCommentList);
}