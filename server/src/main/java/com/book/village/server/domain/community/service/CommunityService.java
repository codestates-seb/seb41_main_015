package com.book.village.server.domain.community.service;

import com.book.village.server.domain.community.entity.Community;
import com.book.village.server.domain.community.repository.CommunityRepository;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
