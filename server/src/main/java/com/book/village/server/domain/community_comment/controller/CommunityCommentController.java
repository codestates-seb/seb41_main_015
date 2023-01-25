package com.book.village.server.domain.community_comment.controller;

import com.book.village.server.domain.community_comment.dto.CommunityCommentDto;
import com.book.village.server.domain.community_comment.entity.CommunityComment;
import com.book.village.server.domain.community_comment.mapper.CommunityCommentMapper;
import com.book.village.server.domain.community_comment.repository.CommunityCommentRepository;
import com.book.village.server.domain.community_comment.service.CommunityCommentService;
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
@RequestMapping("/v1/communities/comments")
public class CommunityCommentController {
    private final CommunityCommentService service;
    private final CommunityCommentMapper mapper;

    public CommunityCommentController(CommunityCommentService service, CommunityCommentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/{community-id}")
    public ResponseEntity postCommunityComment(@PathVariable("community-id") long communityId,
                                               @Valid @RequestBody CommunityCommentDto.Post cCommentPostDto,
                                               Principal principal){
        CommunityComment cComment = service.createCommunityComment(mapper.communityCommentPostDtoToCommunityComment(cCommentPostDto),
                principal.getName(), communityId);
        return new ResponseEntity(new SingleResponse<>(mapper.communityCommentToCommunityCommentResponseDto(cComment)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{communityComment-id}")
    public ResponseEntity patchCommunityComment(@PathVariable("communityComment-id") long cCommentId,
                                                @Valid @RequestBody CommunityCommentDto.Patch cCommentPatchDto,
                                                Principal principal){
        cCommentPatchDto.setCommunityCommentId(cCommentId);
        CommunityComment cComment = service.updateCommunityComment(mapper.communityCommentPatchDtoToCommunityComment(cCommentPatchDto),
                principal.getName());
        return ResponseEntity.ok(new SingleResponse<>(mapper.communityCommentToCommunityCommentResponseDto(cComment)));
    }

    @GetMapping("/{communityComment-id}")
    public ResponseEntity getCommunityComment(@PathVariable("communityComment-id") long cCommentId){
        CommunityComment cComment = service.findCommunityComment(cCommentId);
        return ResponseEntity.ok(new SingleResponse<>(mapper.communityCommentToCommunityCommentResponseDto(cComment)));
    }

    @GetMapping("/mine")
    public ResponseEntity getMyCommunityComments(Principal principal){
        List<CommunityComment> communityCommentList = service.findMyCommunityComments(principal.getName());
        return ResponseEntity.ok(new SingleResponse<>(mapper.communityCommentsToCommunityCommentResponseDtos(communityCommentList)));
    }

    @DeleteMapping("/{communityComment-id}")
    public ResponseEntity deleteCommunityComment(@PathVariable("communityComment-id") long cCommentId,
                                                 Principal principal){
        service.deleteCommunityComment(cCommentId, principal.getName());
        return ResponseEntity.noContent().build();
    }


}
