package com.book.village.server.domain.community_comment.service;

import com.book.village.server.domain.community.service.CommunityService;
import com.book.village.server.domain.community_comment.entity.CommunityComment;
import com.book.village.server.domain.community_comment.repository.CommunityCommentRepository;
import com.book.village.server.domain.member.service.MemberService;
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
public class CommunityCommentService {
    private final CommunityCommentRepository repository;
    private final MemberService memberService;
    private final CommunityService communityService;
    private final CustomBeanUtils<CommunityComment> beanUtils;

    public CommunityCommentService(CommunityCommentRepository repository, MemberService memberService, CommunityService communityService, CustomBeanUtils<CommunityComment> beanUtils) {
        this.repository = repository;
        this.memberService = memberService;
        this.communityService = communityService;
        this.beanUtils = beanUtils;
    }
    public CommunityComment createCommunityComment(CommunityComment cComment, String email, long communityId){
        cComment.setMember(memberService.findMember(email));
        cComment.setCommunity(communityService.findCommunity(communityId));
        cComment.setDisplayName(memberService.findMember(email).getDisplayName());
        return repository.save(cComment);
    }
    public CommunityComment updateCommunityComment(CommunityComment cComment, String email){
        CommunityComment findcComment = findVerifiedCommunityComment(cComment.getCommunityCommentId());
        verifyWriter(findcComment, email);
        beanUtils.copyNonNullProperties(cComment,findcComment);
        return repository.save(findcComment);
    }

    public CommunityComment findCommunityComment(long cCommentId){
        return findVerifiedCommunityComment(cCommentId);
    }

    public List<CommunityComment> findMyCommunityComments(String email){
        Sort lateSort = Sort.by("createdAt").descending();
        return repository.findAllByMember_Email(email, lateSort);
    }

    public void deleteCommunityComment(long cCommentId, String email){
        CommunityComment cComment = findVerifiedCommunityComment(cCommentId);
        verifyWriter(cComment, email);
        repository.delete(cComment);
    }

    public CommunityComment findVerifiedCommunityComment(long cCommentId){
        Optional<CommunityComment> optionalcComment = repository.findById(cCommentId);
        CommunityComment cComment =
                optionalcComment.orElseThrow(()->
                        new CustomLogicException(ExceptionCode.COMMUNITY_COMMENT_NOT_FOUND));
        return cComment;
    }

    public void verifyWriter(CommunityComment cComment, String email){
        if(!cComment.getMember().getEmail().equals(email))
            throw new CustomLogicException(ExceptionCode.COMMUNITY_COMMENT_USER_DIFFERENT);
    }

}
