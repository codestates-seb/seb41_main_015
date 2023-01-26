package com.book.village.server.domain.request_comment.service;

import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.domain.request.service.RequestService;
import com.book.village.server.domain.request_comment.entity.RequestComment;
import com.book.village.server.domain.request_comment.repository.RequestCommentRepository;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RequestCommentService {
    private final MemberService memberService;

    private final RequestService requestService;
    private final RequestCommentRepository requestCommentRepository;

    private final CustomBeanUtils<RequestComment> beanUtils;

    public RequestCommentService(MemberService memberService, RequestService requestService, RequestCommentRepository requestCommentRepository, CustomBeanUtils<RequestComment> beanUtils) {
        this.memberService = memberService;
        this.requestService = requestService;
        this.requestCommentRepository = requestCommentRepository;
        this.beanUtils = beanUtils;
    }

    public RequestComment createRequestComment(RequestComment requestComment, String userEmail, long requestId) {
        requestComment.setMember(memberService.findMember(userEmail));
        requestComment.setRequest(requestService.findRequest(requestId));
        requestComment.setDisplayName(memberService.findMember(userEmail).getDisplayName());
        return requestCommentRepository.save(requestComment);
    }

    public RequestComment updateRequestComment(RequestComment requestComment, String userEmail) {
        RequestComment findRequestComment = findVerifiedRequestComment(requestComment.getRequestCommentId());
        if (findRequestComment.getMember().getEmail().equals(userEmail)) {
            beanUtils.copyNonNullProperties(requestComment, findRequestComment);
            return requestCommentRepository.save(findRequestComment);
        }
        throw new CustomLogicException(ExceptionCode.REQUEST_COMMENT_USER_DIFFERENT);
    }

    public RequestComment findRequestComment(long requestCommentId) {
        return findVerifiedRequestComment(requestCommentId);
    }

    public List<RequestComment> findMyRequestComments(String userEmail) {
        Sort lateSort = Sort.by("createdAt").descending();
        return requestCommentRepository.findAllByMember_Email(userEmail, lateSort);
    }

    public void deleteRequestComment(long requestCommentId, String userEmail) {
        RequestComment requestComment = findVerifiedRequestComment(requestCommentId);
        if (requestComment.getMember().getEmail().equals(userEmail)) {
            requestCommentRepository.delete(requestComment);
            return ;
        }
        throw new CustomLogicException(ExceptionCode.REQUEST_COMMENT_USER_DIFFERENT);
    }

    private RequestComment findVerifiedRequestComment(long requestCommentId) {
        Optional<RequestComment> optionalrequestComment = requestCommentRepository.findById(requestCommentId);
        RequestComment requestComment =
                optionalrequestComment.orElseThrow(()->
                        new CustomLogicException(ExceptionCode.REQUEST_COMMENT_NOT_FOUND));
        return requestComment;
    }
}
