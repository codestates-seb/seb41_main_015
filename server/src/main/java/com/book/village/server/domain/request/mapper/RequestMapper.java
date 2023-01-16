package com.book.village.server.domain.request.mapper;

import com.book.village.server.domain.request.dto.RequestDto;
import com.book.village.server.domain.request.entity.Request;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    default Request requestPostDtoToRequest(RequestDto.Post requestPostDto) {
        if (requestPostDto == null) {
            return null;
        }
        Request request = Request.builder()
                .talkUrl(requestPostDto.getTalkUrl())
                .title(requestPostDto.getTitle())
                .content(requestPostDto.getContent())
                .bookTitle(requestPostDto.getBookTitle())
                .author(requestPostDto.getAuthor())
                .publisher(requestPostDto.getPublisher())
                .build();

        return request;
    }


    default RequestDto.Response requestToRequestResponseDto(Request request) {
        return RequestDto.Response.builder()
                .requestId(request.getRequestId())
                .talkUrl(request.getTalkUrl())
                .title(request.getTitle())
                .content(request.getContent())
                .bookTitle(request.getBookTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .displayName(request.getMember().getDisplayName())
                .requestComments(request.getRequestComments())
                .createdAt(request.getCreatedAt())
                .modifiedAt(request.getModifiedAt())
                .build();
    }

    Request requestPatchDtoToRequest(RequestDto.Patch requestPatchDto);

    List<RequestDto.Response> requestsToRequestResponseDtos(List<Request> myRequests);
}