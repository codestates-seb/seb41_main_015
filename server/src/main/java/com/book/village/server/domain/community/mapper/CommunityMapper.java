package com.book.village.server.domain.community.mapper;

import com.book.village.server.domain.community.dto.CommunityDto;
import com.book.village.server.domain.community.entity.Community;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    Community postCommunityDtoToCommunity(CommunityDto.Post communityPostDto);
    Community patchCommunityDtoToCommunity(CommunityDto.Patch communityPatchDto);
    CommunityDto.Response communityToCommunityResponseDto(Community community);
    List<CommunityDto.Response> communitiesToCommunityResponseDtos(List<Community> communities);
}
