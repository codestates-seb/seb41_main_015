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

    // λκΈ μμ±
    public BorrowComment createBorrowComment(BorrowComment borrowComment, String email, Long borrowId) {
        borrowComment.setMember(memberService.findMember(email));
        borrowComment.setBorrow(borrowService.findBorrow(borrowId));
        borrowComment.setDisplayName(memberService.findMember(email).getDisplayName());
        return borrowCommentRepository.save(borrowComment);
    }

    // λκ΅΄ μμ 
    public BorrowComment updateBorrowComment(BorrowComment borrowComment, String email) {
        BorrowComment findBorrowComment =
                findVerifiedBorrowComment(borrowComment.getBorrowCommentId());
        verificationBorrowComment(findBorrowComment, email);
        beanUtils.copyNonNullProperties(borrowComment, findBorrowComment);
        return borrowCommentRepository.save(findBorrowComment);
    }

    // λκΈ νμΈ (λμ€μ λ¨μΌ λλκΈ μ‘°νμ λκΈ λΆλ¬μ¬ λ μ¬μ©.)
    public BorrowComment findBorrowComment(Long borrowCommentId) {
        return findVerifiedBorrowComment(borrowCommentId);
    }

    // BorrowComments
     public List<BorrowComment> findMyBorrowComments(String email) {
        Sort lateSort = Sort.by("createdAt").descending();
        return borrowCommentRepository.findAllByMember_Email(email, lateSort);
     }

     // λκΈ μ­μ 
     public void deleteBorrowComment(Long borrowCommentId, String email) {
        // λκΈμ μ‘΄μ¬ μ λ¬΄ νμΈ
        BorrowComment borrowComment = findVerifiedBorrowComment(borrowCommentId);
        // λκΈμ μ­μ νλ €λ μ¬λμ΄ μμ±ν μ¬λμΈμ§ νμΈ
        verificationBorrowComment(borrowComment, email);
        borrowCommentRepository.delete(borrowComment);
     }

    // μμ νλ €λ μ μ μ μμ±νλ €λ μ μ κ° λμΌν μ¬λμΈμ§
    private void verificationBorrowComment(BorrowComment findBorrowComment, String email) {
        if(!findBorrowComment.getMember().getEmail().equals(email)) {
            throw new CustomLogicException(ExceptionCode.BORROW_COMMENT_USER_DIFFERENT);
        }
    }

    // BorrowComment μ‘΄μ¬ μ λ¬΄
    private BorrowComment findVerifiedBorrowComment(Long borrowCommentId) {
        Optional<BorrowComment> optionalBorrowComment = borrowCommentRepository.findById(borrowCommentId);
        BorrowComment borrowComment =
                optionalBorrowComment.orElseThrow(() ->
                new CustomLogicException(ExceptionCode.BORROW_COMMENT_NOT_FOUND));
        return borrowComment;
    }

}
