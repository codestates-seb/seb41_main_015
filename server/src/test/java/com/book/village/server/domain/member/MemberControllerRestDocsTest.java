package com.book.village.server.domain.member;

import com.book.village.server.domain.member.controller.MemberController;
import com.book.village.server.domain.member.dto.MemberDto;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.member.mapper.MemberMapper;
import com.book.village.server.domain.member.service.MemberService;
import com.google.gson.Gson;
import org.aspectj.weaver.patterns.TypePatternQuestions;
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
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
    public void patchQuestionTest() throws Exception {
        long memberId=1L;
        MemberDto.Patch patch = MemberDto.Patch.builder()
                .name("name1")
                .displayName("user1")
                .address("address1")
                .phoneNumber("010-1234-5678")
                .build();

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;
        String content = gson.toJson(patch);

        MemberDto.Response responseDto = MemberDto.Response.builder()
                .memberId(memberId)
                .email("test@gmail.com")
                .name("name1")
                .displayName("user1")
                .phoneNumber("010-1234-5678")
                .memberStatus(Member.MemberStatus.MEMBER_ACTIVE.getStatus())
                .build();


        given(mapper.patchMemberDtoToMember(Mockito.any(MemberDto.Patch.class))).willReturn(new Member());

        given(memberService.updateMember(Mockito.any(Member.class),Mockito.any(Member.class))).willReturn(new Member());

        given(mapper.memberToResponseMemberDto(Mockito.any(Member.class))).willReturn(responseDto);


        ResultActions actions =
                mockMvc.perform(
                        patch(url)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GeneratedToken.getMockHeaderToken())
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questionId").value(patch.getQuestionId()))
                .andExpect(jsonPath("$.data.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andDo(document("patch-question",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("question-id").description("질문 식별자")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("questionId").type(JsonFieldType.NUMBER).description("질문 식별자").ignored(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목").optional(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용").optional(),
                                        fieldWithPath("tag").type(JsonFieldType.ARRAY).description("태그").optional()
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.questionId").type(JsonFieldType.NUMBER).description("질문 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("data.voteResult").type(JsonFieldType.NUMBER).description("투표 결과"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("질문 작성자"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("질문 작성자 이메일"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("질문 작성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("질문 수정 일자"),
                                        fieldWithPath("data.tag").type(JsonFieldType.ARRAY).description("태그"),
                                        fieldWithPath("data.answers").type(JsonFieldType.ARRAY).description("답변"),
                                        fieldWithPath("data.answerCount").type(JsonFieldType.NUMBER).description("답변 개수"),
                                        fieldWithPath("data.answers.[].answerId").type(JsonFieldType.NUMBER).description("답변 식별자"),
                                        fieldWithPath("data.answers.[].content").type(JsonFieldType.STRING).description("답변 내용"),
                                        fieldWithPath("data.answers.[].voteResult").type(JsonFieldType.NUMBER).description("답변 투표"),
                                        fieldWithPath("data.answers.[].questionId").type(JsonFieldType.NUMBER).description("답변 내부 질문 식별자"),
                                        fieldWithPath("data.answers.[].displayName").type(JsonFieldType.STRING).description("답변 작성자"),
                                        fieldWithPath("data.answers.[].email").type(JsonFieldType.STRING).description("답변 작성자 이메일"),
                                        fieldWithPath("data.answers.[].createdAt").type(JsonFieldType.STRING).description("답변 생성 일자"),
                                        fieldWithPath("data.answers.[].modifiedAt").type(JsonFieldType.STRING).description("답변 수정 일자")
                                )
                        )
                ));
    }


}
