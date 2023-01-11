package com.book.village.server.domain.request;


import com.book.village.server.domain.member.service.MemberService;
import com.book.village.server.domain.request.controller.RequestController;
import com.book.village.server.domain.request.dto.RequestDto;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.domain.request.mapper.RequestMapper;
import com.book.village.server.domain.request.service.RequestService;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class RequestControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @MockBean
    private RequestMapper requestMapper;

    @Autowired
    private Gson gson;

    private static final String BASE_URL = "/v1/requests";

    @Test
    @DisplayName("요청 게시글 생성")
    @WithMockUser
    public void createRequestTest() throws Exception {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = LocalDateTime.now();
        RequestDto.Post post = new RequestDto.Post("talkUrl", "title", "content", "bookTitle", "author", "publisher");
        String content = gson.toJson(post);
        RequestDto.Response responseDto =
                new RequestDto.Response(1L,
                        "talkUrl",
                        "title",
                        "content",
                        "bookTitle",
                        "author",
                        "publisher",
                        "displayName",
                        createdAt,
                        modifiedAt);

        given(requestMapper.requestPostDtoToRequest(Mockito.any(RequestDto.Post.class))).willReturn(new Request());

        given(requestService.createRequest(Mockito.any(Request.class), Mockito.anyString())).willReturn(new Request());

        given(requestMapper.requestToRequestResponseDto(Mockito.any(Request.class))).willReturn(responseDto);


        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken()));

        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.talkUrl").value(post.getTalkUrl()))
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.bookTitle").value(post.getBookTitle()))
                .andExpect(jsonPath("$.data.author").value(post.getAuthor()))
                .andExpect(jsonPath("$.data.publisher").value(post.getPublisher()))
                .andDo(document("post-request",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("저자"),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사")
                                )

                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.requestId").type(JsonFieldType.NUMBER).description("요청 식별자"),
                                        fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("저자"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("출판사"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("요청 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("요청 수정 일자")
                                )
                        )
                ));


    }

    @Test
    @DisplayName("요청 게시글 수정")
    @WithMockUser
    public void patchRequestTest() throws Exception {
        long requestId = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = LocalDateTime.now();
        RequestDto.Patch patch = new RequestDto.Patch(
                requestId,
                "talkUrl",
                "title",
                "content",
                "bookTitle",
                "author",
                "publisher");
        String content = gson.toJson(patch);

        RequestDto.Response response =
                new RequestDto.Response(1L, "talkUrl",
                        "title",
                        "content",
                        "bookTitle",
                        "author",
                        "publisher",
                        "displayName",
                        createdAt,
                        modifiedAt);



        given(requestMapper.requestPatchDtoToRequest(Mockito.any(RequestDto.Patch.class))).willReturn(new Request());
        given(requestService.updateRequest(Mockito.any(Request.class), Mockito.anyString())).willReturn(new Request());
        given(requestMapper.requestToRequestResponseDto(Mockito.any(Request.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/{request-id}", requestId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken()));

        actions
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.data.requestId").value(patch.getRequestId()))
                .andExpect(jsonPath("$.data.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andExpect(jsonPath("$.data.bookTitle").value(patch.getBookTitle()))
                .andExpect(jsonPath("$.data.author").value(patch.getAuthor()))
                .andExpect(jsonPath("$.data.publisher").value(patch.getPublisher()))
                .andDo(document("patch-request",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("requestId").type(JsonFieldType.NUMBER).description("요청 식별자"),
                                        fieldWithPath("talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("저자"),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사")
                                )

                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.requestId").type(JsonFieldType.NUMBER).description("요청 식별자"),
                                        fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("게시글 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("저자"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("출판사"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("요청 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("요청 수정 일자")
                                )
                        )
                ));

    }
}
