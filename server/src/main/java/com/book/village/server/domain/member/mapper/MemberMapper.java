package com.book.village.server.domain.member.mapper;

import com.book.village.server.domain.member.dto.MemberDto;
import com.book.village.server.domain.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member patchMemberDtoToMember(MemberDto.Patch memberPatchDto);
    default MemberDto.Response memberToResponseMemberDto(Member member){
        if (member == null) {
            return null;
        } else {
            MemberDto.Response.ResponseBuilder response = MemberDto.Response.builder();
            response.memberId(member.getMemberId());
            response.email(member.getEmail());
            response.name(member.getName());
            response.displayName(member.getDisplayName());
            response.imgUrl(member.getImgUrl());
            response.phoneNumber(member.getPhoneNumber());
            response.address(member.getAddress());
            response.memberStatus(member.getMemberStatus().getStatus());
            response.createdAt(member.getCreatedAt());
            response.modifiedAt(member.getModifiedAt());
            return response.build();
        }
    };
}
