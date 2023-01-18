package com.book.village.server.domain.request.mapper;

import com.book.village.server.domain.community.dto.CommunityDto;
import com.book.village.server.domain.community.entity.Community;
import com.book.village.server.domain.request.dto.RequestDto;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.domain.request_comment.mapper.RequestCommentMapper;
import com.book.village.server.domain.request_comment.mapper.RequestCommentMapperImpl;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    RequestCommentMapper recoMapper = new RequestCommentMapperImpl();
    Request requestPostDtoToRequest(RequestDto.Post requestPostDto);
    Request requestPatchDtoToRequest(RequestDto.Patch requestPatchDto);


    default RequestDto.Response requestToRequestResponseDto(Request request) {
        if (request == null) {
            return null;
        }
        RequestDto.Response response = new RequestDto.Response();
                response.setRequestId(request.getRequestId());
                response.setTalkUrl(request.getTalkUrl());
                response.setTitle(request.getTitle());
                response.setContent(request.getContent());
                response.setBookTitle(request.getBookTitle());
                response.setAuthor(request.getAuthor());
                response.setPublisher(request.getPublisher());
                response.setDisplayName(request.getMember().getDisplayName());
                response.setRequestComments(request.getRequestComments().stream()
                        .map(requestComment -> recoMapper.requestCommentToRequestCommentResponseDto(requestComment))
                        .collect(Collectors.toList()));
                response.setCreatedAt(request.getCreatedAt());
                response.setModifiedAt(request.getModifiedAt());
        return response;
    }



    default List<RequestDto.Response> requestsToRequestResponseDtos(List<Request> requests) {
        if ( requests == null ) {
            return null;
        }

        List<RequestDto.Response> list = new ArrayList<RequestDto.Response>( requests.size() );
        for ( Request request : requests ) {
            RequestDto.Response response = requestToRequestResponseDto( request );
            response.setRequestComments(null);
            list.add(response);
        }
        return list;
    }
}