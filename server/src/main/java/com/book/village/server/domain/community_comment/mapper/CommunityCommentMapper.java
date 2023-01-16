package com.book.village.server.domain.community_comment.mapper;

import com.book.village.server.domain.community_comment.dto.CommunityCommentDto;
import com.book.village.server.domain.community_comment.entity.CommunityComment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommunityCommentMapper {
    CommunityComment communityCommentPostDtoToCommunityComment(CommunityCommentDto.Post communityCommentPostDto);
    CommunityComment communityCommentPatchDtoToCommunityComment(CommunityCommentDto.Patch communityCommentPatchDto);
    CommunityCommentDto.Response communityCommentToCommunityCommentResponseDto(CommunityComment cComment);
    List<CommunityCommentDto.Response> communityCommentsToCommunityCommentResponseDtos(List<CommunityComment> communityComments);
}
