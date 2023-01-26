package com.book.village.server.domain.request_comment.mapper;

import com.book.village.server.domain.request_comment.dto.RequestCommentDto;
import com.book.village.server.domain.request_comment.entity.RequestComment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestCommentMapper {

    RequestComment requestCommentPostDtoToRequestComment(RequestCommentDto.Post requestCommentPostDto);

    RequestComment requestCommentPatchDtoToRequestComment(RequestCommentDto.Patch requestCommentPatchDto);

    default RequestCommentDto.Response requestCommentToRequestCommentResponseDto(RequestComment requestComment){
        if (requestComment == null) {
            return null;
        } else {
            RequestCommentDto.Response response = new RequestCommentDto.Response();
            response.setRequestCommentId(requestComment.getRequestCommentId());
            response.setContent(requestComment.getContent());
            response.setDisplayName(requestComment.getDisplayName());
            response.setImgUrl(requestComment.getMember().getImgUrl());
            response.setCreatedAt(requestComment.getCreatedAt());
            response.setModifiedAt(requestComment.getModifiedAt());
            return response;
        }
    }

    List<RequestCommentDto.Response> requestCommentsToRequestCommentResponseDtos(List<RequestComment> requestCommentList);
}