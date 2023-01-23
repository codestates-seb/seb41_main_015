package com.book.village.server.domain.borrowcomment.controller;

import com.book.village.server.domain.borrowcomment.dto.BorrowCommentDto;
import com.book.village.server.domain.borrowcomment.entity.BorrowComment;
import com.book.village.server.domain.borrowcomment.mapper.BorrowCommentMapper;
import com.book.village.server.domain.borrowcomment.service.BorrowCommentService;
import com.book.village.server.global.response.SingleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/v1/borrows/comments")
public class BorrowCommentController {
    private final BorrowCommentService borrowCommentService;
    private final BorrowCommentMapper borrowCommentMapper;


    public BorrowCommentController(BorrowCommentService borrowCommentService,
                                   BorrowCommentMapper borrowCommentMapper) {
        this.borrowCommentService = borrowCommentService;
        this.borrowCommentMapper = borrowCommentMapper;
    }

    // 코멘트 등록
    @PostMapping("/{borrow-Id}")
    public ResponseEntity postBorrowComment(@PathVariable("borrow-Id") Long borrowId,
                                            @Validated @RequestBody BorrowCommentDto.Post borrowCommentDtoPost, Principal principal) {
        BorrowComment borrowComment = borrowCommentMapper.borrowCommentPostDtoToBorrowComment(borrowCommentDtoPost);
        BorrowComment createdBorrowComment = borrowCommentService.createBorrowComment(borrowComment, principal.getName(), borrowId);

        return new ResponseEntity(new SingleResponse<>(borrowCommentMapper.borrowCommentToBorrowCommentResponseDto(createdBorrowComment)),
                HttpStatus.CREATED);
    }

    // 코멘트 수정
    @PatchMapping("/{borrowComment-Id}")
    public ResponseEntity patchBorrowComment(@PathVariable("borrowComment-Id") Long borrowCommentId,
                                             @Validated @RequestBody BorrowCommentDto.Patch borrowCommentDtoPatch, Principal principal) {
        borrowCommentDtoPatch.setBorrowCommentId(borrowCommentId);
        BorrowComment borrowComment = borrowCommentMapper.borrowCommentPatchDtoToBorrowComment(borrowCommentDtoPatch);
        BorrowComment updatedBorrowComment = borrowCommentService.updateBorrowComment(borrowComment, principal.getName());
        return new ResponseEntity(new SingleResponse<>(borrowCommentMapper.borrowCommentToBorrowCommentResponseDto(updatedBorrowComment)),
                HttpStatus.OK);
    }

    @GetMapping("/{borrowComment-id}")
    public ResponseEntity getBorrowComment(@PathVariable("borrowComment-id") Long borrowCommentId) {
        BorrowComment borrowComment = borrowCommentService.findBorrowComment(borrowCommentId);
        return new ResponseEntity(new SingleResponse<>(borrowCommentMapper.borrowCommentToBorrowCommentResponseDto(borrowComment)),
                HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity getMyBorrowComments(Principal principal) {
        List<BorrowComment> borrowComments =
                borrowCommentService.findMyBorrowComments(principal.getName());
        return new ResponseEntity(new SingleResponse<>(borrowCommentMapper.borrowCommentsToBorrowCommentResponseDtos(borrowComments)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{borrowComment-id}")
    public ResponseEntity deleteBorrowComment(@PathVariable("borrowComment-id") Long borrowCommentId,
                                              Principal principal) {
        borrowCommentService.deleteBorrowComment(borrowCommentId, principal.getName());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
