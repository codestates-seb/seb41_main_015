package com.book.village.server.domain.borrow.service;

import com.book.village.server.domain.borrow.entity.Borrow;
import com.book.village.server.domain.borrow.repository.BorrowRepository;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    public Borrow createBorrow(Borrow borrow, String userEmail) {
        borrow.setMember(memberService.findMember(userEmail));  // 이메일로 인한 유저멤버 변경
        borrow.setDisplayName(borrow.getMember().getDisplayName()); // 닉네임 유저 닉네임으로 변경.
        return borrowRepository.save(borrow);
    }

    // Borrow 수정
    public Borrow updateBorrow(Borrow borrow, String userEamil) {
        Borrow findBorrow = findVerificationBorrow(borrow.getBorrowId());   // 게시글 유무 확인.
        verificationBorrow(findBorrow, userEamil);  // 회원 이메일로 나눔글 작성자와 수정할 사람이 동일한 이메일인지 확인
        customBeanUtils.copyNonNullProperties(borrow, findBorrow);
        return borrowRepository.save(findBorrow);
    }

    // borrow 한개
    public Borrow findBorrow(Long borrowId) {
        return findVerificationBorrow(borrowId);
    }

    // Borrow 전체
    public Page<Borrow> findBorrows(Pageable pageable) {
        return borrowRepository.findAll(pageable);
    }

    public Page<Borrow> findMyBorrows(String userEmail, Pageable pageable) {
        return borrowRepository.findAllByMember_Email(userEmail, pageable);
    }

    public void deleteBorrow(Long borrowId, String userEmail) {
        Borrow findBorrow = findVerificationBorrow(borrowId);
        verificationBorrow(findBorrow, userEmail);
        borrowRepository.delete(findBorrow);
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
        if(!findBorrow.getMember().getEmail().equals(userEmail)) {
            new CustomLogicException(ExceptionCode.BORROW_USER_DIFFERENT);
        }
        // API 계층으로 받은 Email과 Borrow의 작성자 Email이 맞는지 확인.
        // 다른 유저라면 Exception 던짐.
    }

    public Page<Borrow> searchBorrow(String keyword, String field, Pageable pageable) {
        switch(field) {
            case "title" :
                return borrowRepository.findAllByTitleContaining(keyword, pageable);
            case "content" :
                return borrowRepository.findAllByContentContaining(keyword, pageable);
            case "displayName" :
                return borrowRepository.findAllByDisplayName(keyword, pageable);
            case "bookTitle":
                return borrowRepository.findAllByBookTitleContaining(keyword, pageable);
            case "author":
                return borrowRepository.findAllByAuthor(keyword, pageable);
            case "publisher":
                return borrowRepository.findAllByPublisher(keyword, pageable);
            default:
                return new PageImpl<>(Collections.emptyList());
        }
    }

}
