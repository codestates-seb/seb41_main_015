package com.book.village.server.domain.community.mapper;

import com.book.village.server.domain.community.dto.CommunityDto;
import com.book.village.server.domain.community.entity.Community;
import com.book.village.server.domain.community_comment.mapper.CommunityCommentMapper;
import com.book.village.server.domain.community_comment.mapper.CommunityCommentMapperImpl;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    CommunityCommentMapper commentMapper=new CommunityCommentMapperImpl();
    Community postCommunityDtoToCommunity(CommunityDto.Post communityPostDto);
    Community patchCommunityDtoToCommunity(CommunityDto.Patch communityPatchDto);
    default CommunityDto.Response communityToCommunityResponseDto(Community community){
        if ( community == null ) {
            return null;
        }

        CommunityDto.Response response = new CommunityDto.Response();

        response.setCommunityId( community.getCommunityId() );
        response.setType( community.getType() );
        response.setTitle( community.getTitle() );
        response.setContent( community.getContent() );
        response.setDisplayName( community.getDisplayName() );
        response.setView(community.getView());
        response.setImgUrl(community.getMember().getImgUrl());
        response.setCommunityComments(community.getCommunityComments().stream()
                .map(cComment->commentMapper.communityCommentToCommunityCommentResponseDto(cComment))
                .collect(Collectors.toList()));
        response.setCreatedAt( community.getCreatedAt() );
        response.setModifiedAt( community.getModifiedAt() );

        return response;
    }
    default List<CommunityDto.Response> communitiesToCommunityResponseDtos(List<Community> communities){
        if ( communities == null ) {
            return null;
        }

        List<CommunityDto.Response> list = new ArrayList<CommunityDto.Response>( communities.size() );
        for ( Community community : communities ) {
            CommunityDto.Response response = communityToCommunityResponseDto( community );
            response.setCommunityComments(null);
            list.add( response );
        }

        return list;
    }
}
