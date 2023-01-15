package com.book.village.server.domain.borrowcomment.service;

import com.book.village.server.domain.borrow.repository.BorrowRepository;
import com.book.village.server.domain.borrow.service.BorrowService;
import com.book.village.server.domain.borrowcomment.entity.BorrowComment;
import com.book.village.server.domain.borrowcomment.repository.BorrowCommentRepository;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class BorrowCommentService {
    private final BorrowCommentRepository borrowCommentRepository;
    private final MemberService memberService;
    private final BorrowService borrowService;
    private final CustomBeanUtils<BorrowComment> beanUtils;
    private final BorrowRepository borrowRepository;

    public BorrowCommentService(BorrowCommentRepository borrowCommentRepository,
                                MemberService memberService,
                                BorrowService borrowService,
                                CustomBeanUtils beanUtils,
                                BorrowRepository borrowRepository) {
        this.borrowCommentRepository = borrowCommentRepository;
        this.memberService = memberService;
        this.borrowService = borrowService;
        this.beanUtils = beanUtils;
        this.borrowRepository = borrowRepository;
    }

    // 댓글 생성
    public BorrowComment createBorrowComment(BorrowComment borrowComment, String email, Long borrowId) {
        borrowComment.setMember(memberService.findMember(email));
        borrowComment.setBorrow(borrowService.findBorrow(borrowId));
        borrowComment.setDisplayName(memberService.findMember(email).getDisplayName());
        return borrowCommentRepository.save(borrowComment);
    }

    // 댓굴 수정
    public BorrowComment updateBorrowComment(BorrowComment borrowComment, String email) {
        BorrowComment findBorrowComment =
                findVerifiedBorrowComment(borrowComment.getBorrowCommentId());
        verificationBorrowComment(findBorrowComment, email);
        beanUtils.copyNonNullProperties(borrowComment, findBorrowComment);
        return borrowCommentRepository.save(findBorrowComment);
    }

    // 댓글 확인 (나중에 단일 나눔글 조회시 댓글 불러올 때 사용.)
    public BorrowComment findBorrowComment(Long borrowCommentId) {
        return findVerifiedBorrowComment(borrowCommentId);
    }

    // BorrowComments
     public List<BorrowComment> findMyBorrowComments(String email) {
        Sort lateSort = Sort.by("createdAt").descending();
        return borrowCommentRepository.findAllByMember_Email(email, lateSort);
     }

     // 댓글 삭제
     public void deleteBorrowComment(Long borrowCommentId, String email) {
        // 댓글의 존재 유무 확인
        BorrowComment borrowComment = findVerifiedBorrowComment(borrowCommentId);
        // 댓글을 삭제하려는 사람이 작성한 사람인지 확인
        verificationBorrowComment(borrowComment, email);
        borrowCommentRepository.delete(borrowComment);
     }

    // 수정하려는 유저와 작성하려는 유저가 동일한 사람인지
    private void verificationBorrowComment(BorrowComment findBorrowComment, String email) {
        if(!findBorrowComment.getMember().getEmail().equals(email)) {
            throw new CustomLogicException(ExceptionCode.BORROW_COMMENT_USER_DIFFERENT);
        }
    }

    // BorrowComment 존재 유무
    private BorrowComment findVerifiedBorrowComment(Long borrowCommentId) {
        Optional<BorrowComment> optionalBorrowComment = borrowCommentRepository.findById(borrowCommentId);
        BorrowComment borrowComment =
                optionalBorrowComment.orElseThrow(() ->
                new CustomLogicException(ExceptionCode.BORROW_COMMENT_NOT_FOUND));
        return borrowComment;
    }

}
