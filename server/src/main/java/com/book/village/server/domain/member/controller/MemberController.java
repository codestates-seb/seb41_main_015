package com.book.village.server.domain.member.controller;

import com.book.village.server.auth.jwt.service.RefreshTokenService;
import com.book.village.server.domain.member.dto.MemberDto;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.member.mapper.MemberMapper;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.response.MessageResponseDto;
import com.book.village.server.global.response.SingleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @PatchMapping
    public ResponseEntity patchMember(Principal principal, @Valid @RequestBody MemberDto.Patch memberDto) {
        Member member = memberService.findMember(principal.getName());
        memberService.updateMember(member, memberMapper.patchMemberDtoToMember(memberDto));
        return ResponseEntity.ok(new SingleResponse<>(memberMapper.memberToResponseMemberDto(member)));
    }

    @GetMapping
    public ResponseEntity getMember(Principal principal){
        Member member = memberService.findMember(principal.getName());
        return ResponseEntity.ok(new SingleResponse<>(memberMapper.memberToResponseMemberDto(member)));
    }
    @PostMapping("/auth/logout")
    public ResponseEntity logoutMember(HttpServletRequest request, Principal principal) {

        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        memberService.registerLogoutToken(jws, principal.getName());

        return ResponseEntity.ok(new MessageResponseDto("logout completed!"));
    }



    @PatchMapping("/quit")
    public ResponseEntity quitMember(Principal principal){
        memberService.quitMember(principal.getName());
        return ResponseEntity.ok(new MessageResponseDto("quit member!"));
    }
}
