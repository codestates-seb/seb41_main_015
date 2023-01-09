package com.book.village.server.domain.member.mapper;

import com.book.village.server.domain.member.dto.MemberDto;
import com.book.village.server.domain.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member patchMemberDtoToMember(MemberDto.Patch memberDto);
    MemberDto.Response memberToResponseMemberDto(Member member);
}
