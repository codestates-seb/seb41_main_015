package com.book.village.server.domain.member;

import com.book.village.server.domain.member.controller.MemberController;
import com.book.village.server.domain.member.dto.MemberDto;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.member.mapper.MemberMapper;
import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.global.utils.GenerateMockToken;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.book.village.server.util.ApiDocumentUtils.getRequestPreProcessor;
import static com.book.village.server.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MemberControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberMapper mapper;

    @Autowired
    private Gson gson;

    private static final String url = "/v1/members";

    @Test
    @DisplayName("회원 수정")
    @WithMockUser
    public void patchMemberTest() throws Exception {
        long memberId=1L;
        MemberDto.Patch patch = new MemberDto.Patch(
                memberId,
                "name1",
                "user1",
                "https://img.icons8.com/windows/32/null/user-male-circle.png",
                "address1",
                "010-1234-5678");

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;
        String content = gson.toJson(patch);

        MemberDto.Response responseDto = new MemberDto.Response(
                memberId,
                "test1234@gmail.com",
                "name1",
                "user1",
                "https://img.icons8.com/windows/32/null/user-male-circle.png",
                "address1",
                "010-1234-5678",
                Member.MemberStatus.MEMBER_ACTIVE.getStatus(),
                createdAt,
                modifiedAt
        );

        given(mapper.patchMemberDtoToMember(Mockito.any(MemberDto.Patch.class))).willReturn(new Member());
        given(memberService.findMember(Mockito.anyString())).willReturn(new Member());
        given(memberService.updateMember(Mockito.any(Member.class),Mockito.any(Member.class))).willReturn(new Member());

        given(mapper.memberToResponseMemberDto(Mockito.any(Member.class))).willReturn(responseDto);


        ResultActions actions =
                mockMvc.perform(
                        patch(url)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value(patch.getName()))
                .andExpect(jsonPath("$.data.displayName").value(patch.getDisplayName()))
                .andExpect(jsonPath("$.data.imgUrl").value(patch.getImgUrl()))
                .andExpect(jsonPath("$.data.address").value(patch.getAddress()))
                .andExpect(jsonPath("$.data.phoneNumber").value(patch.getPhoneNumber()))
                .andDo(document("patch-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored(),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름").optional(),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("닉네임").ignored(),
                                        fieldWithPath("imgUrl").type(JsonFieldType.STRING).description("이미지 url").optional(),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("주소").optional(),
                                        fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("핸드폰 번호").optional()
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("이미지 url"),
                                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("주소"),
                                        fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("핸드폰 번호"),
                                        fieldWithPath("data.memberStatus").type(JsonFieldType.STRING).description("회원 상태"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("회원 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("회원 조회")
    @WithMockUser
    public void getMemberTest() throws Exception {
        long memberId=1L;

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;

        MemberDto.Response responseDto = new MemberDto.Response(
                memberId,
                "test1234@gmail.com",
                "name1",
                "user1",
                "https://img.icons8.com/windows/32/null/user-male-circle.png",
                "address1",
                "010-1234-5678",
                Member.MemberStatus.MEMBER_ACTIVE.getStatus(),
                createdAt,
                modifiedAt
        );

        given(memberService.findMember(Mockito.anyString())).willReturn(new Member());
        given(mapper.memberToResponseMemberDto(Mockito.any(Member.class))).willReturn(responseDto);


        ResultActions actions =
                mockMvc.perform(
                        get(url)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberId").value(responseDto.getMemberId()))
                .andExpect(jsonPath("$.data.email").value(responseDto.getEmail()))
                .andExpect(jsonPath("$.data.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.data.displayName").value(responseDto.getDisplayName()))
                .andExpect(jsonPath("$.data.imgUrl").value(responseDto.getImgUrl()))
                .andExpect(jsonPath("$.data.address").value(responseDto.getAddress()))
                .andExpect(jsonPath("$.data.phoneNumber").value(responseDto.getPhoneNumber()))
                .andDo(document("get-member",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("imgUrl"),
                                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("주소"),
                                        fieldWithPath("data.phoneNumber").type(JsonFieldType.STRING).description("핸드폰 번호"),
                                        fieldWithPath("data.memberStatus").type(JsonFieldType.STRING).description("회원 상태"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("회원 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("회원 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃")
    @WithMockUser
    public void logoutMemberTest() throws Exception {
        long memberId=1L;

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;

        doNothing().when(memberService).registerLogoutToken(Mockito.anyString(),Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        post(url+"/auth/logout")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("logout completed!"))
                .andDo(document("logout-member",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("로그아웃 메세지")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("회원 탈퇴")
    @WithMockUser
    public void quitMemberTest() throws Exception {
        long memberId=1L;

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;

        doNothing().when(memberService).quitMember(Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        patch(url+"/quit")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("quit member!"))
                .andDo(document("quit-member",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("탈퇴 메세지")
                                )
                        )
                ));
    }
}
