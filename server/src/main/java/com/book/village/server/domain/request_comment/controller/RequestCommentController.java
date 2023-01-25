package com.book.village.server.domain.request_comment.controller;

import com.book.village.server.domain.request_comment.dto.RequestCommentDto;
import com.book.village.server.domain.request_comment.entity.RequestComment;
import com.book.village.server.domain.request_comment.mapper.RequestCommentMapper;
import com.book.village.server.domain.request_comment.service.RequestCommentService;
import com.book.village.server.global.response.SingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@Validated
@RequestMapping("/v1/requests/comments")
public class RequestCommentController {
    private final RequestCommentService requestCommentService;
    private final RequestCommentMapper mapper;

    public RequestCommentController(RequestCommentService requestCommentService, RequestCommentMapper mapper) {
        this.requestCommentService = requestCommentService;
        this.mapper = mapper;
    }


    @PostMapping("/{request-id}")
    public ResponseEntity postRequestComment(@PathVariable("request-id") long requestId,
                                             Principal principal,
                                             @Valid @RequestBody RequestCommentDto.Post requestCommentPostDto) {
        RequestComment requestComment = requestCommentService.createRequestComment(
                mapper.requestCommentPostDtoToRequestComment(requestCommentPostDto), principal.getName(), requestId);
        return new ResponseEntity(
                new SingleResponse<>(mapper.requestCommentToRequestCommentResponseDto(requestComment)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{requestComment-id}")
    public ResponseEntity patchRequestComment(@PathVariable("requestComment-id") long requestCommentId,
                                                @Valid @RequestBody RequestCommentDto.Patch requestCommentPatchDto,
                                                Principal principal){
        requestCommentPatchDto.setRequestCommentId(requestCommentId);
        RequestComment requestComment = requestCommentService.updateRequestComment(mapper.requestCommentPatchDtoToRequestComment(requestCommentPatchDto),
                principal.getName());
        return ResponseEntity.ok(new SingleResponse<>(mapper.requestCommentToRequestCommentResponseDto(requestComment)));
    }

    @GetMapping("/{requestComment-id}")
    public ResponseEntity getRequestComment(@PathVariable("requestComment-id") long requestCommentId){
        RequestComment requestComment = requestCommentService.findRequestComment(requestCommentId);
        return ResponseEntity.ok(new SingleResponse<>(mapper.requestCommentToRequestCommentResponseDto(requestComment)));
    }

    @GetMapping("/mine")
    public ResponseEntity getMyRequestComments(Principal principal){
        List<RequestComment> requestCommentList = requestCommentService.findMyRequestComments(principal.getName());
        return ResponseEntity.ok(new SingleResponse<>(mapper.requestCommentsToRequestCommentResponseDtos(requestCommentList)));
    }

    @DeleteMapping("/{requestComment-id}")
    public ResponseEntity deleteRequestComment(@PathVariable("requestComment-id") long requestCommentId,
                                                 Principal principal){
        requestCommentService.deleteRequestComment(requestCommentId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}



