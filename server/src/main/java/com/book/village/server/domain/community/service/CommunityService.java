package com.book.village.server.domain.community.service;

import com.book.village.server.domain.community.entity.Community;
import com.book.village.server.domain.community.repository.CommunityRepository;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class CommunityService {
    private final CommunityRepository repository;
    private final MemberService memberService;
    private final CustomBeanUtils<Community> beanUtils;

    public CommunityService(CommunityRepository repository, MemberService memberService, CustomBeanUtils<Community> beanUtils) {
        this.repository = repository;
        this.memberService = memberService;
        this.beanUtils = beanUtils;
    }

    public Community createCommunity(Community community, String email){
        community.setMember(memberService.findMember(email));
        community.setDisplayName(community.getMember().getDisplayName());
        return repository.save(community);
    }
    public Community updateCommunity(Community community, String email){
        Community findCommunity = findVerifiedCommunity(community.getCommunityId());
        verifyWriter(findCommunity, email);
        beanUtils.copyNonNullProperties(community,findCommunity);
        return repository.save(findCommunity);
    }

    public Community findCommunity(long communityId){
        return findVerifiedCommunity(communityId);
    }

    public Page<Community> findCommunities(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Community> findMyCommunities(String email,Pageable pageable){
        return repository.findAllByMember_Email(email, pageable);
    }

    public void deleteCommunity(long communityId, String email){
        Community findCommunity=findVerifiedCommunity(communityId);
        verifyWriter(findCommunity,email);
        repository.delete(findCommunity);
    }

    public void verifyWriter(Community community, String email){
        if(!community.getMember().getEmail().equals(email))
            throw new CustomLogicException(ExceptionCode.COMMUNITY_USER_DIFFERENT);
    }

    public Community findVerifiedCommunity(long communityId){
        Optional<Community> optionalCommunity = repository.findById(communityId);
        Community community =
                optionalCommunity.orElseThrow(()->
                        new CustomLogicException(ExceptionCode.COMMUNITY_NOT_FOUND));
        return community;
    }

    public Page<Community> searchCommunity(String keyword, String field, String type, Pageable pageable){
        switch(field){
            case "title":
                return repository.findAllByTitleContainingAndType(keyword, type, pageable);
            case "content":
                return repository.findAllByContentContainingAndType(keyword, type, pageable);
            case "displayName":
                return repository.findAllByDisplayNameAndType(keyword, type, pageable);
            default:
                return new PageImpl<>(Collections.emptyList());
        }
    }
}
