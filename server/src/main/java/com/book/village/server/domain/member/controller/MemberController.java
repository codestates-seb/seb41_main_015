package com.book.village.server.domain.member.controller;

import com.book.village.server.domain.member.dto.MemberDto;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.member.mapper.MemberMapper;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.response.SingleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@Validated
@RequestMapping("/v1/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private static final String BASE_URL = "/v1/members";

    public MemberController(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    @GetMapping
    public ResponseEntity getMember(Principal principal){
        Member member = memberService.findMember(principal.getName());
        return ResponseEntity.ok(new SingleResponse<>(memberMapper.memberToResponseMemberDto(member)));
    }
    @PatchMapping
    public ResponseEntity patchMember(Principal principal, @Valid @RequestBody MemberDto.Patch memberDto) {
        Member member = memberService.findMember(principal.getName());
        memberService.updateMember(member, memberMapper.patchMemberDtoToMember(memberDto));
        return ResponseEntity.ok(new SingleResponse<>(memberMapper.memberToResponseMemberDto(member)));
    }
}
