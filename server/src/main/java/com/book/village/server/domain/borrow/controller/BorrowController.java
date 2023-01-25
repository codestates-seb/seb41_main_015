package com.book.village.server.domain.borrow.controller;

import com.book.village.server.domain.borrow.dto.BorrowDto;
import com.book.village.server.domain.borrow.entity.Borrow;
import com.book.village.server.domain.borrow.entity.BorrowRank;
import com.book.village.server.domain.borrow.mapper.BorrowMapper;
import com.book.village.server.domain.borrow.service.BorrowService;
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

import java.security.Principal;
import java.util.List;

@RestController
@Validated
@RequestMapping("/v1/borrows")
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
                                     @Validated @RequestBody BorrowDto.Post borrowPostDto) {

        Borrow borrow = borrowMapper.borrowDtoPostToBorrow(borrowPostDto);
        borrowService.createBorrow(borrow, principal.getName());
        return new ResponseEntity<>(new SingleResponse<>(borrowMapper.borrowToBorrowDtoResponse(borrow)), HttpStatus.CREATED);
    }

    // Borrow 수정
    @PatchMapping("/{borrow-id}")
    public ResponseEntity patchBorrow(Principal principal,
                                      @Validated @RequestBody BorrowDto.Patch borrowPatch,
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
        getBorrow.setViewCount(getBorrow.getViewCount()+1);
        borrowService.updateBorrow(getBorrow, getBorrow.getMember().getEmail());
        // 결과가 나오면 return
        return new ResponseEntity(new SingleResponse<>(borrowMapper.borrowToBorrowDtoResponse(getBorrow)),
                HttpStatus.OK);
    }

    // Borrow 전체조회
    @GetMapping
    public ResponseEntity getBorrows(@PageableDefault Pageable pageable){
        Page<Borrow> borrows = borrowService.findBorrows(pageable);
        return  new ResponseEntity<>(
                new PageResponseDto<>(borrowMapper.borrowsToBorrowResponseDtos(borrows.getContent()),
                        new PageInfo(borrows.getPageable(), borrows.getTotalElements())), HttpStatus.OK);
    }

    @GetMapping("/mine")
    public ResponseEntity getMyBorrows(@PageableDefault Pageable pageable, Principal principal){
        Page<Borrow> borrows = borrowService.findMyBorrows(principal.getName(), pageable);
        return new ResponseEntity<>(
                new PageResponseDto<>(borrowMapper.borrowsToBorrowResponseDtos(borrows.getContent()),
                        new PageInfo(borrows.getPageable(), borrows.getTotalElements())), HttpStatus.OK);
    }

    @DeleteMapping("/{borrow-id}")
    public ResponseEntity deleteBorrow(@PathVariable("borrow-id") Long borrowId, Principal principal) {
        borrowService.deleteBorrow(borrowId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity searchBorrow(@RequestParam String keyword , @RequestParam String field, @PageableDefault Pageable pageable){
        Page<Borrow> borrows = borrowService.searchBorrow(keyword, field, pageable);
        return new ResponseEntity<>(new PageResponseDto<>(borrowMapper.borrowsToBorrowResponseDtos(borrows.getContent()),
                new PageInfo(borrows.getPageable(), borrows.getTotalElements())), HttpStatus.OK);
    }

    @GetMapping("/rank")
    public ResponseEntity BorrowRank() {
        List<BorrowRank> rankResponses = borrowService.findRankedBorrows();
        return new ResponseEntity(
                new ListResponse<>(borrowMapper.borrowRanksTorankedResponses(rankResponses)),HttpStatus.OK);
    }


}
