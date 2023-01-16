package com.book.village.server.domain.request.controller;
import com.book.village.server.domain.request.dto.RequestDto;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.domain.request.mapper.RequestMapper;
import com.book.village.server.domain.request.service.RequestService;
import com.book.village.server.global.response.ListResponse;
import com.book.village.server.global.response.PageInfo;
import com.book.village.server.global.response.PageResponseDto;
import com.book.village.server.global.response.SingleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@Validated
@RequestMapping("/v1/requests")
public class RequestController {
    private final RequestService requestService;
    private final RequestMapper requestMapper;

    public RequestController(RequestService requestService, RequestMapper requestMapper) {
        this.requestService = requestService;
        this.requestMapper = requestMapper;
    }

    @PostMapping
    public ResponseEntity postRequest(Principal principal,
                                      @Valid @RequestBody RequestDto.Post requestPostDto) {
        Request request = requestMapper.requestPostDtoToRequest(requestPostDto);
        requestService.createRequest(request, principal.getName());
        return new ResponseEntity(
                new SingleResponse<>(requestMapper.requestToRequestResponseDto(request)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{request-id}")
    public ResponseEntity patchRequest(Principal principal,
                                       @PathVariable("request-id") long requestId,
                                       @Valid @RequestBody RequestDto.Patch requestPatchDto) {
        requestPatchDto.setRequestId(requestId);
        Request request = requestService.updateRequest(requestMapper.requestPatchDtoToRequest(requestPatchDto), principal.getName());
        return new ResponseEntity(
                new SingleResponse<>(requestMapper.requestToRequestResponseDto(request)), HttpStatus.OK);
    }

    @GetMapping("/{request-id}")
    public ResponseEntity getRequest(@PathVariable("request-id") long requestId) {
        Request request = requestService.findRequest(requestId);
        return new ResponseEntity(new SingleResponse<>(requestMapper.requestToRequestResponseDto(request)),
                HttpStatus.OK);

    }

    @GetMapping("/mine")
    public ResponseEntity getMyRequests(Principal principal) {
        List<Request> myRequests = requestService.findMyRequests(principal.getName());
        return new ResponseEntity(new ListResponse<>(requestMapper.requestsToRequestResponseDtos(myRequests)),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getRequests(@PageableDefault Pageable pageable) {
        Page<Request> requests = requestService.findRequests(pageable);
        return new ResponseEntity(
                new PageResponseDto<>(requestMapper.requestsToRequestResponseDtos(requests.getContent()),
                        new PageInfo(requests.getPageable(), requests.getTotalElements())), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity searchRequest(@RequestParam String keyword,
                                        @RequestParam String kind,
                                        @PageableDefault Pageable pageable) {
        Page<Request> requests = requestService.searchRequests(keyword, kind, pageable);
        return new ResponseEntity(
                new PageResponseDto<>(requestMapper.requestsToRequestResponseDtos(requests.getContent()),
                        new PageInfo(requests.getPageable(), requests.getTotalElements())), HttpStatus.OK);
    }

    @DeleteMapping("/{request-id}")
    public ResponseEntity deleteRequest(@PathVariable("request-id") long requestId,
                                        Principal principal) {
        requestService.deleteRequest(requestId, principal.getName());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
