package com.book.village.server.domain.community_comment.mapper;

import com.book.village.server.domain.community_comment.dto.CommunityCommentDto;
import com.book.village.server.domain.community_comment.entity.CommunityComment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommunityCommentMapper {
    CommunityComment communityCommentPostDtoToCommunityComment(CommunityCommentDto.Post communityCommentPostDto);
    CommunityComment communityCommentPatchDtoToCommunityComment(CommunityCommentDto.Patch communityCommentPatchDto);
    default CommunityCommentDto.Response communityCommentToCommunityCommentResponseDto(CommunityComment cComment){
        if (cComment == null) {
            return null;
        } else {
            CommunityCommentDto.Response response = new CommunityCommentDto.Response();
            response.setCommunityCommentId(cComment.getCommunityCommentId());
            response.setContent(cComment.getContent());
            response.setDisplayName(cComment.getDisplayName());
            response.setImgUrl(cComment.getMember().getImgUrl());
            response.setCreatedAt(cComment.getCreatedAt());
            response.setModifiedAt(cComment.getModifiedAt());
            return response;
        }
    }
    List<CommunityCommentDto.Response> communityCommentsToCommunityCommentResponseDtos(List<CommunityComment> communityComments);
}
