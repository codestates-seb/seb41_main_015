package com.book.village.server.domain.borrow.service;

import com.book.village.server.domain.borrow.entity.Borrow;
import com.book.village.server.domain.borrow.repository.BorrowRepository;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final MemberService memberService;
    private final CustomBeanUtils customBeanUtils;

    public BorrowService(BorrowRepository borrowRepository,
                         MemberService memberService,
                         CustomBeanUtils customBeanUtils) {
        this.borrowRepository = borrowRepository;
        this.memberService = memberService;
        this.customBeanUtils = customBeanUtils;
    }

    // Borrow 생성
        // 생성시 딱히 검증할 필요가 있는 게 없음. 회원은 이미 인증 인가된 상태.
    public Borrow createBorrow(Borrow borrow, String userEmail) {

        borrow.setMember(memberService.findMember(userEmail));  // 이메일로 인한 유저멤버 변경

        return borrowRepository.save(borrow);
    }

    // Borrow 수정
    public Borrow updateBorrow(Borrow borrow, String userEamil) {
        Borrow findBorrow = findVerificationBorrow(borrow.getBorrowId());   // 게시글 유무 확인.
        // 회원은 이미 검증됨.
        // 존재하는 게시판인지
            // 존재하지 않다면, 예외처리
            // 존재하면 save
        // 게시판 쓴 유저와 동일한지
            // 동일하지 않다면 예외처리
            // 동일하면,
        // 위의 내용을 한번에 검증
        verificationBorrow(findBorrow, userEamil);  // 회원 이메일로 나눔글 작성자와 수정할 사람이 동일한 이메일인지 확인
        customBeanUtils.copyNonNullProperties(borrow, findBorrow);
        // (sourse, destination) 원래의 findBorrow를 Patch시 넘겨받은 Borrow로 수정한다.

        return borrowRepository.save(findBorrow);
    }


    // 나눔글 수정시 나눔글 존재 유뮤 확인
    public Borrow findVerificationBorrow(Long borrowId) {
        // 게시글 존재 유무
        Optional<Borrow> OptionalBorrow = borrowRepository.findById(borrowId);  // 나눔글 정보 DB에서 조회.
        Borrow findBorrow = OptionalBorrow.orElseThrow(() ->
            new CustomLogicException(ExceptionCode.BORROW_NOT_FOUND)); // 만약 null이면 없다고 에러

        return findBorrow;
    }

    // 게시글 존재 유무 확인 후, 작성자 회원과 수정할 회원이 동일한 이메일인지 확인
    public void verificationBorrow(Borrow findBorrow, String userEmail) {
        if(findBorrow.getMember().getEmail() == userEmail) {    // API 계층으로 받은 Email과
            return;                                             // Borrow의 작성자 Email이 맞는지 확인.
        } else new CustomLogicException(ExceptionCode.BORROW_USER_DIFFERENT); // 다른 유저라면 Exception 던짐.
    }








}
