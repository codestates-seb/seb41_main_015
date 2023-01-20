package com.book.village.server.domain.borrowcomment.controller;

import com.book.village.server.domain.borrow.entity.Borrow;
import com.book.village.server.domain.borrowcomment.dto.BorrowCommentDto;
import com.book.village.server.domain.borrowcomment.entity.BorrowComment;
import com.book.village.server.domain.borrowcomment.mapper.BorrowCommentMapper;
import com.book.village.server.domain.borrowcomment.service.BorrowCommentService;
import com.book.village.server.domain.member.entity.Member;
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
import java.util.ArrayList;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BorrowCommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class BorrowCommentControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowCommentService borrowCommentService;

    @MockBean
    private BorrowCommentMapper borrowCommentMapper;

    @Autowired
    private Gson gson;

    private static final String BASE_URL = "/v1/borrows/comments";

    private static final LocalDateTime time = LocalDateTime.now();

    @Test
    @DisplayName("나눔 댓글 등록")
    @WithMockUser
    void postBorrowComment() throws Exception {
        Long borrowId = 1L;
        Long borrowCommentId = 1L;

        BorrowCommentDto.Post post= new BorrowCommentDto.Post(
                "content1"
        );
        LocalDateTime createdAt  = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        String content = gson.toJson(post);

        BorrowCommentDto.Response response = new BorrowCommentDto.Response(
                borrowCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                modifiedAt
        );

        given(borrowCommentMapper.borrowCommentPostDtoToBorrowComment(Mockito.any(BorrowCommentDto.Post.class))).willReturn(new BorrowComment());
        given(borrowCommentService.createBorrowComment(Mockito.any(BorrowComment.class), Mockito.anyString(), Mockito.anyLong())).willReturn(new BorrowComment());
        given(borrowCommentMapper.borrowCommentToBorrowCommentResponseDto(Mockito.any(BorrowComment.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL+"/{borrow-id}", borrowId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andDo(document("post-borrow-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("borrow-id").description("나눔 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.borrowCommentId").type(JsonFieldType.NUMBER).description("나눔 댓글 식별자"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("댓글 작성자 프로필 이미지"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("나눔 댓글 수정")
    @WithMockUser
    void patchBorrowComment() throws Exception {
        Long borrowCommentId = 1L;
        BorrowCommentDto.Patch patch = new BorrowCommentDto.Patch(
                borrowCommentId,
                "borrowCommentContent1"
        );
        LocalDateTime createdAt = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        String content = gson.toJson(patch);

        BorrowCommentDto.Response response = new BorrowCommentDto.Response(
                borrowCommentId,
                "borrowCommentContent1",
                "displayName1",
                "imgUrl1",
                createdAt,
                modifiedAt
        );

        given(borrowCommentMapper.borrowCommentPatchDtoToBorrowComment(Mockito.any(BorrowCommentDto.Patch.class))).willReturn(new BorrowComment());
        given(borrowCommentService.updateBorrowComment(Mockito.any(BorrowComment.class), Mockito.anyString())).willReturn(new BorrowComment());
        given(borrowCommentMapper.borrowCommentToBorrowCommentResponseDto(Mockito.any(BorrowComment.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL+"/{borrowComment-id}", borrowCommentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andDo(document("patch-borrow-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("borrowComment-id").description("나눔 댓글 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("borrowCommentId").type(JsonFieldType.STRING).description("나눔 댓글 식별자").ignored(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용").optional()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.borrowCommentId").type(JsonFieldType.NUMBER).description("나눔 댓글 식별자"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("나눔 댓글 내용"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("댓글 작서자 프로필 이미지"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("나눔 댓글 조회")
    @WithMockUser
    public void getBorrowComment() throws Exception{
        long cCommentId= 1L;

        LocalDateTime createdAt  = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        BorrowCommentDto.Response response= new BorrowCommentDto.Response(
                cCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                modifiedAt
        );

        given(borrowCommentService.findBorrowComment(Mockito.anyLong())).willReturn(new BorrowComment());
        given(borrowCommentMapper.borrowCommentToBorrowCommentResponseDto(Mockito.any(BorrowComment.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL+"/{borrowComment-id}", cCommentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("get-borrow-comment",
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("borrowComment-id").description("나눔 댓글 식별자")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.borrowCommentId").type(JsonFieldType.NUMBER).description("나눔 댓글 식별자"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("댓글 작성자 프로필 이미지"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                )
                        )
                ));
    }


    @Test
    @DisplayName("나의 모든 나눔 댓글 조회")
    @WithMockUser
    void getMyBorrowCommentsTest() throws Exception {
        LocalDateTime createdAt  = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        BorrowCommentDto.Response response1 = new BorrowCommentDto.Response(
                1L,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                modifiedAt
        );

        BorrowCommentDto.Response response2 = new BorrowCommentDto.Response(
                2L,
                "content2",
                "displayName2",
                "imgUrl2",
                createdAt,
                modifiedAt
        );

        List<BorrowCommentDto.Response> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        List<BorrowComment> borrowCommentList = new ArrayList<>();
        borrowCommentList.add(
                new BorrowComment(
                        1L,
                        "content1",
                        "displayName1",
                        new Borrow(),
                        new Member()
        ));

        borrowCommentList.add(
                new BorrowComment(
                        2L,
                        "content2",
                        "displayName2",
                        new Borrow(),
                        new Member()
        ));

        given(borrowCommentService.findMyBorrowComments(Mockito.anyString())).willReturn(borrowCommentList);
        given(borrowCommentMapper.borrowCommentsToBorrowCommentResponseDtos(Mockito.anyList())).willReturn(responseList);

        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/mine")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        actions
                .andExpect(status().isOk())
                .andDo(document("get-borrow-comment-mine",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].borrowCommentId").type(JsonFieldType.NUMBER).description("나눔 댓글 식별자"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                        fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("댓글 작성자 프로필 이미지"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("커뮤니티 댓글 삭제")
    @WithMockUser
    void deleteBorrowCommentTest() throws Exception {
        Long borrowCommentId = 1L;

        LocalDateTime createdAt  = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        BorrowCommentDto.Response response = new BorrowCommentDto.Response(
                borrowCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                modifiedAt
        );

        doNothing().when(borrowCommentService).deleteBorrowComment(Mockito.anyLong(),Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        delete(BASE_URL + "/{borrowComment-id}", borrowCommentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-borrow-comment",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("borrowComment-id").description("나눔 댓글 식별자")
                        )
                ));
    }
}