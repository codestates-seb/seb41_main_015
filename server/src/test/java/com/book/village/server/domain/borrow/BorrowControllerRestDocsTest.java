package com.book.village.server.domain.borrow;

import com.book.village.server.domain.borrow.controller.BorrowController;
import com.book.village.server.domain.borrow.dto.BorrowDto;
import com.book.village.server.domain.borrow.entity.Borrow;
import com.book.village.server.domain.borrow.mapper.BorrowMapper;
import com.book.village.server.domain.borrow.service.BorrowService;
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
import org.springframework.http.HttpHeaders;
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
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BorrowController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class BorrowControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowMapper borrowMapper;

    @MockBean
    private BorrowService borrowService;

    @Autowired
    private Gson gson;

    private static final String BASE_URL = "/v1/borrow";

    @Test
    @DisplayName("나눔 게시글 작성😊😊😊😊")
    @WithMockUser
    void postBorrowTest() throws Exception {
        // given
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = LocalDateTime.now();
        BorrowDto.Post post =
                new BorrowDto.Post("title",
                "content",
                "bookTitle",
                "author",
                "publisher",
                "displayName",
                "talkUrl");

        String content = gson.toJson(post);

        BorrowDto.Response responseDto =
                new BorrowDto.Response(1L,
                        "title",
                        "content",
                        "bookTitle",
                        "author",
                        "publisher",
                        "displayName",
                        "talkUrl",
                        createdAt,
                        modifiedAt);



        given(borrowMapper.borrowDtoPostToBorrow(Mockito.any(BorrowDto.Post.class))).willReturn(new Borrow());

        given(borrowService.createBorrow(Mockito.any(Borrow.class), Mockito.anyString())).willReturn(new Borrow());

        given(borrowMapper.borrowToBorrowDtoResponse(Mockito.any(Borrow.class))).willReturn(responseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken()));


        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.bookTitle").value(post.getBookTitle()))
                .andExpect(jsonPath("$.data.author").value(post.getAuthor()))
                .andExpect(jsonPath("$.data.publisher").value(post.getPublisher()))
                .andExpect(jsonPath("$.data.displayName").value(post.getDisplayName()))
                .andExpect(jsonPath("$.data.talkUrl").value(post.getTalkUrl()))
                .andDo(document("post-borrow",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("나눔게시글 본문"),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description(" 제목"),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("나눔 게시글 작성자 닉네임"),
                                        fieldWithPath("talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.borrowId").type(JsonFieldType.NUMBER).description("나눔 게시글 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("나눌 책 제목"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("나눔 게시글 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("나눔 게시글 최신 수정 일자")
                                )
                        )

                ));
    }


    @Test
    @DisplayName("나눔 게시글 수정😊😊😊😊😊")
    @WithMockUser
    void patchBorrowTest() throws Exception {
        // given
        long borrowId = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = LocalDateTime.now();

        BorrowDto.Patch patch =
                new BorrowDto.Patch(borrowId,
                        "title",
                        "content",
                        "bookTitle",
                        "author",
                        "publisher",
                        "talkUrl");
        String content = gson.toJson(patch);

        BorrowDto.Response responseDto =
                new BorrowDto.Response(1L,
                        "title",
                        "content",
                        "bookTitle",
                        "author",
                        "publisher",
                        "displayName",
                        "talkUrl",
                        createdAt,
                        modifiedAt);

//        String accessToken = "tokenexample";
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.AUTHORIZATION, accessToken);


        given(borrowMapper.borrowDtoPatchToBorrow(Mockito.any(BorrowDto.Patch.class))).willReturn(new Borrow());

        given(borrowService.updateBorrow(Mockito.any(Borrow.class), Mockito.anyString())).willReturn(new Borrow());

        given(borrowMapper.borrowToBorrowDtoResponse(Mockito.any(Borrow.class))).willReturn(responseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/{borrow-id}", borrowId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken()));


        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.borrowId").value(patch.getBorrowId()))
                .andExpect(jsonPath("$.data.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andExpect(jsonPath("$.data.bookTitle").value(patch.getBookTitle()))
                .andExpect(jsonPath("$.data.author").value(patch.getAuthor()))
                .andExpect(jsonPath("$.data.publisher").value(patch.getPublisher()))
                .andExpect(jsonPath("$.data.talkUrl").value(patch.getTalkUrl()))
                .andDo(document("patch-borrow",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("borrow-id").description("나눔 식별자")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("borrowId").type(JsonFieldType.NUMBER).description("나눔 식별자").ignored(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("나눔게시글 제목").optional(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("나눔게시글 본문").optional(),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description(" 제목").optional(),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("나눌 책 저자").optional(),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("나눌 책 출판사").optional(),
                                        fieldWithPath("talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크").optional()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.borrowId").type(JsonFieldType.NUMBER).description("나눔 게시글 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("나눌 책 제목"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("나눔 게시글 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("나눔 게시글 최신 수정 일자")
                                )
                        )

                ));
    }

}