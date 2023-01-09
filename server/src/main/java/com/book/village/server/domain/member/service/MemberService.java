package com.book.village.server.domain.member.service;

import com.book.village.server.auth.utils.CustomAuthorityUtils;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.member.repository.MemberRepository;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import com.book.village.server.global.utils.CustomBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomBeanUtils<Member> customBeanUtils;
    private final CustomAuthorityUtils authorityUtils;

    public MemberService(MemberRepository memberRepository, CustomBeanUtils<Member> customBeanUtils, CustomAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.customBeanUtils = customBeanUtils;
        this.authorityUtils = authorityUtils;
    }

    public Member createMember(Member member) {
        findByEmail(member.getEmail()).ifPresent(m -> {
            throw new CustomLogicException(ExceptionCode.MEMBER_DUPLICATE);
        });

        member.setMemberStatus(Member.MemberStatus.MEMBER_ACTIVE);
        member.setRoles(authorityUtils.createRoles(member.getEmail()));
        return memberRepository.save(member);
    }

    private Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member findMember(String email) {
        return findByEmail(email).orElseThrow(() -> new CustomLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member updateMember(Member member, Member patchMember) {
        return customBeanUtils.copyNonNullProperties(patchMember, member);
    }
}
