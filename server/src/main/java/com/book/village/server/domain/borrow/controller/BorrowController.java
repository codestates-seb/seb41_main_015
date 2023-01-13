package com.book.village.server.domain.borrow.controller;

import com.book.village.server.domain.borrow.dto.BorrowDto;
import com.book.village.server.domain.borrow.entity.Borrow;
import com.book.village.server.domain.borrow.mapper.BorrowMapper;
import com.book.village.server.domain.borrow.service.BorrowService;
import com.book.village.server.global.response.SingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Validated
@RequestMapping("/v1/borrow")
public class BorrowController {
    private final BorrowMapper borrowMapper;
    private final BorrowService borrowService;

    public BorrowController(BorrowMapper borrowMapper,
                            BorrowService borrowService) {
        this.borrowMapper = borrowMapper;
        this.borrowService = borrowService;
    }

    // Borrow 생성
    @PostMapping
    public ResponseEntity postBorrow(Principal principal,
                                     @RequestBody BorrowDto.Post borrowPostDto) {
        Borrow borrow = borrowMapper.borrowDtoPostToBorrow(borrowPostDto);
        Borrow createdBorrow = borrowService.createBorrow(borrow, principal.getName());

        return new ResponseEntity<>(new SingleResponse<>(borrowMapper.borrowToBorrowDtoResponse(createdBorrow)), HttpStatus.CREATED);
    }

    // Borrow 수정
    @PatchMapping("/{borrow-id}")
    public ResponseEntity patchBorrow(Principal principal,
                                      @RequestBody BorrowDto.Patch borrowPatch,
                                      @PathVariable("borrow-id") Long borrowId) {
        borrowPatch.setBorrowId(borrowId);
        Borrow borrow = borrowMapper.borrowDtoPatchToBorrow(borrowPatch);    // 엔티티로 매핑

        Borrow updatedBorrow = borrowService.updateBorrow(borrow, principal.getName());

        return new ResponseEntity<>(new SingleResponse<>(borrowMapper.borrowToBorrowDtoResponse(updatedBorrow)), HttpStatus.OK);
    }

//     Borrow 조회
//      그냥 조회는 인증 굳이 필요없음.
    @GetMapping("/{borrow-id}")
    public ResponseEntity getBorrow(@PathVariable("borrow-id")Long borrowId) {
        // 서비스클래스에서 검증처리 됨.
        Borrow getBorrow = borrowService.findVerificationBorrow(borrowId);
        // 결과가 나오면 return
        return new ResponseEntity(new SingleResponse<>(borrowMapper.borrowToBorrowDtoResponse(getBorrow)), HttpStatus.OK);
    }
}
