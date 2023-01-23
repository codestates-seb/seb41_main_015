package com.book.village.server.domain.request;

import com.book.village.server.domain.request.controller.RequestController;
import com.book.village.server.domain.request.dto.RequestDto;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.domain.request.mapper.RequestMapper;
import com.book.village.server.domain.request.service.RequestService;
import com.book.village.server.domain.request_comment.dto.RequestCommentDto;
import com.book.village.server.domain.request_comment.entity.RequestComment;
import com.book.village.server.global.utils.GenerateMockToken;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
        RequestDto.Post post = new RequestDto.Post("talkUrl", "title", "content", "bookTitle", "author", "publisher","thumbnail");
        String content = gson.toJson(post);
        List<RequestCommentDto.Response> requestCommentResponse =
                List.of(new RequestCommentDto.Response(
                                1L, "content1", "displayName1","imgUrl1",createdAt, modifiedAt),
                        new RequestCommentDto.Response(2L, "content2", "displayName2","imgUrl2",createdAt, modifiedAt)
                );

        RequestDto.Response responseDto =
                new RequestDto.Response(1L,
                        "talkUrl",
                        "title",
                        "content",
                        "bookTitle",
                        "author",
                        "publisher",
                        "thumbnail",
                        "displayName",
                        0L,
                        "imgUrl",
                        requestCommentResponse,
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
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
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
                                        fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("책 이미지"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.views").type(JsonFieldType.NUMBER).description("요청 조회수"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("요청 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("요청 수정 일자"),
                                        fieldWithPath("data.requestComments").type(JsonFieldType.ARRAY).description("댓글 정보"),
                                        fieldWithPath("data.requestComments.[].requestCommentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("data.requestComments.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("data.requestComments.[].displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                        fieldWithPath("data.requestComments.[].createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.requestComments.[].modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
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
                "publisher",
                "thumbnail");
        String content = gson.toJson(patch);

        List<RequestCommentDto.Response> requestCommentResponse =
                List.of(new RequestCommentDto.Response(
                                1L, "content1", "displayName1","imgUrl1",createdAt, createdAt),
                        new RequestCommentDto.Response(2L, "content2", "displayName2","imgUrl2",createdAt, createdAt)
                );

        RequestDto.Response response =
                new RequestDto.Response(1L,
                        "talkUrl",
                        "title",
                        "content",
                        "bookTitle",
                        "author",
                        "publisher",
                        "thumbnail",
                        "displayName",
                        0L,
                        "imgUrl",
                        requestCommentResponse,
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
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("request-id").description("요청 식별자")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("requestId").type(JsonFieldType.NUMBER).description("요청 식별자").ignored(),
                                        fieldWithPath("talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크").optional(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목").optional(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 본문").optional(),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("책 제목").optional(),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("저자").optional(),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사").optional(),
                                        fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("책 이미지").optional()
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
                                        fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("책 이미지 프로필"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.views").type(JsonFieldType.NUMBER).description("요청 조회수"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("요청 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("요청 수정 일자"),
                                        fieldWithPath("data.requestComments").type(JsonFieldType.ARRAY).description("댓글 정보"),
                                        fieldWithPath("data.requestComments.[].requestCommentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("data.requestComments.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("data.requestComments.[].displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                        fieldWithPath("data.requestComments.[].imgUrl").type(JsonFieldType.STRING).description("댓글 작성자 프로필 이미지"),
                                        fieldWithPath("data.requestComments.[].createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.requestComments.[].modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                )
                        )
                ));
    }


        @Test
        @DisplayName("요청 조회")
        @WithMockUser
        public void getRequestTest() throws Exception {
            long requestId = 1L;
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime modifiedAt = LocalDateTime.now();

            List<RequestCommentDto.Response> requestCommentResponse =
                    List.of(new RequestCommentDto.Response(
                                    1L, "content1", "displayName1","imgUrl1",createdAt, createdAt),
                            new RequestCommentDto.Response(2L, "content2", "displayName2","imgUrl2",createdAt, createdAt)
                    );

            RequestDto.Response response =
                    new RequestDto.Response(1L,
                            "talkUrl",
                            "title",
                            "content",
                            "bookTitle",
                            "author",
                            "publisher",
                            "thumbnail",
                            "displayName",
                            0L,
                            "imgUrl",
                            requestCommentResponse,
                            createdAt,
                            modifiedAt);

            given(requestService.findRequest(Mockito.anyLong())).willReturn(new Request());
            given(requestMapper.requestToRequestResponseDto(Mockito.any(Request.class))).willReturn(response);

            ResultActions actions =
                    mockMvc.perform(
                            get(BASE_URL + "/{request-id}", requestId)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON));

            actions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.requestId").value(response.getRequestId()))
                    .andExpect(jsonPath("$.data.talkUrl").value(response.getTalkUrl()))
                    .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                    .andExpect(jsonPath("$.data.content").value(response.getContent()))
                    .andExpect(jsonPath("$.data.bookTitle").value(response.getBookTitle()))
                    .andExpect(jsonPath("$.data.author").value(response.getAuthor()))
                    .andExpect(jsonPath("$.data.publisher").value(response.getPublisher()))
                    .andDo(document("get-request" ,
                                    getRequestPreProcessor(),
                                    getResponsePreProcessor(),
                            pathParameters(
                                    parameterWithName("request-id").description("요청 식별자")
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
                                            fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("요청 수정 일자"),
                                            fieldWithPath("data.requestComments").type(JsonFieldType.ARRAY).description("댓글 정보"),
                                            fieldWithPath("data.requestComments.[].requestCommentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                            fieldWithPath("data.requestComments.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                            fieldWithPath("data.requestComments.[].displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                            fieldWithPath("data.requestComments.[].createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                            fieldWithPath("data.requestComments.[].modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                    )
                            )
                    ));
        }

        @Test
        @DisplayName("내 요청 전체 조회")
        @WithMockUser
        public void getMyRequestsTest() throws Exception {
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime modifiedAt = LocalDateTime.now();
            List<RequestComment> requestComments = new ArrayList<>();
            RequestDto.Response response1 =
                    new RequestDto.Response(1L, "talkUrl1",
                            "title1",
                            "content1",
                            "bookTitle1",
                            "author1",
                            "publisher1",
                            "thumbnail1",
                            "displayName1",
                            0L,
                            "imgUrl1",
                            null,
                            createdAt,
                            modifiedAt);
            RequestDto.Response response2 =
                    new RequestDto.Response(2L, "talkUrl2",
                            "title2",
                            "content2",
                            "bookTitle2",
                            "author2",
                            "publisher2",
                            "thumbnail2",
                            "displayName2",
                            0L,
                            "imgUrl2",
                            null,
                            createdAt,
                            modifiedAt);

            List<RequestDto.Response> responseList = new ArrayList<>();
            responseList.add(response1);
            responseList.add(response2);

            List<Request> requestList = new ArrayList<>();
            requestList.add(new Request());
            requestList.add(new Request());

            given(requestService.findMyRequests(Mockito.anyString(),Mockito.any(Pageable.class))).willReturn(new PageImpl<>(
                    requestList,
                    PageRequest.of(0,10,
                            Sort.by("createdAt").descending()),2));
            given(requestMapper.requestsToRequestResponseDtos(Mockito.anyList())).willReturn(responseList);

            ResultActions actions =
                    mockMvc.perform(
                            get(BASE_URL + "/mine")
                                    .param("page", "0")
                                    .param("size", "10")
                                    .param("sort", "questionId,desc")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .with(csrf())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .headers(GenerateMockToken.getMockHeaderToken()));

            actions.
                    andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].title").value(responseList.get(0).getTitle()))
                    .andDo(document("get-myrequests",
                            getRequestPreProcessor(),
                            getResponsePreProcessor(),
                            requestHeaders(
                                    headerWithName("Authorization").description("Bearer Token")
                            ),
                            requestParameters(
                                    parameterWithName("page").description("페이지 번호"),
                                    parameterWithName("size").description("페이지 사이즈"),
                                    parameterWithName("sort").description("정렬 기준[createdAt,desc]"),
                                    parameterWithName("_csrf").description("csrf")
                            ),
                            responseFields(
                                    List.of(
                                            fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                            fieldWithPath("data.[].requestId").type(JsonFieldType.NUMBER).description("요청 식별자"),
                                            fieldWithPath("data.[].talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                            fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                            fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("게시글 본문"),
                                            fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                            fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("저자"),
                                            fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                            fieldWithPath("data.[].thumbnail").type(JsonFieldType.STRING).description("책 이미지"),
                                            fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                            fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지"),
                                            fieldWithPath("data.[].views").type(JsonFieldType.NUMBER).description("요청 조회수"),
                                            fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("요청 생성 일자"),
                                            fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("요청 수정 일자"),
                                            fieldWithPath("data.[].requestComments").type(JsonFieldType.NULL).description("댓글 정보"),
                                            fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                            fieldWithPath("pageInfo.currentPage").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                            fieldWithPath("pageInfo.totalPage").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                            fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 데이터 수"),
                                            fieldWithPath("pageInfo.pageSize").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                                            fieldWithPath("pageInfo.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                            fieldWithPath("pageInfo.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                            fieldWithPath("pageInfo.currentElements").type(JsonFieldType.NUMBER).description("현재 페이지 데이터 수")
                                    )
                            )));
        }


        @Test
        @DisplayName("전체 요청 조회")
        @WithMockUser
        public void getRequestsTest() throws Exception {
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime modifiedAt = LocalDateTime.now();
            List<RequestComment> requestComments = new ArrayList<>();
            RequestDto.Response response1 =
                    new RequestDto.Response(1L,
                            "talkUrl1",
                            "title1",
                            "content1",
                            "bookTitle1",
                            "author1",
                            "publisher1",
                            "thumbnail1",
                            "displayName1",
                            0L,
                            "imgUrl1",
                            null,
                            createdAt,
                            modifiedAt);
            RequestDto.Response response2 =
                    new RequestDto.Response(2L, "talkUrl2",
                            "title2",
                            "content2",
                            "bookTitle2",
                            "author2",
                            "publisher2",
                            "thumbnail2",
                            "displayName2",
                            0L,
                            "imgUrl2",
                            null,
                            createdAt,
                            modifiedAt);

            List<RequestDto.Response> responseList = new ArrayList<>();
            responseList.add(response1);
            responseList.add(response2);

            List<Request> requestList = new ArrayList<>();
            requestList.add(new Request());
            requestList.add(new Request());

            given(requestService.findRequests(Mockito.any(Pageable.class)))
                    .willReturn(
                            new PageImpl<>(requestList,
                                    PageRequest.of(0,
                                            10,
                                            Sort.by("createdAt").descending()
                                    ), 2)
                    );

            given(requestMapper.requestsToRequestResponseDtos(Mockito.anyList())).willReturn(responseList);

            ResultActions actions =
                    mockMvc.perform(get(BASE_URL,
                            getRequestPreProcessor(),
                            getResponsePreProcessor())
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "createdAt,desc") // <-- no space after comma!!!
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                    );

            actions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].title").value(responseList.get(0).getTitle()))
                    .andDo(document("get-requests",
                    getResponsePreProcessor(),
                    requestParameters(
                            parameterWithName("page").description("페이지 번호"),
                            parameterWithName("size").description("페이지 사이즈"),
                            parameterWithName("sort").description("정렬 기준[createdAt,desc]")
                    ),
                    // response body
                    responseFields(
                            List.of(
                                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                    fieldWithPath("data.[].requestId").type(JsonFieldType.NUMBER).description("요청 식별자"),
                                    fieldWithPath("data.[].talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                    fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                    fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("게시글 본문"),
                                    fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                    fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("저자"),
                                    fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                    fieldWithPath("data.[].thumbnail").type(JsonFieldType.STRING).description("책 이미지"),
                                    fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("회원 프로필 이미지"),
                                    fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("회원 닉네임"),
                                    fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                    fieldWithPath("data.[].views").type(JsonFieldType.NUMBER).description("요청 조회수"),
                                    fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("요청 생성 일자"),
                                    fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("요청 수정 일자"),
                                    fieldWithPath("data.[].requestComments").type(JsonFieldType.NULL).description("댓글 정보"),
                                    fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                    fieldWithPath("pageInfo.currentPage").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                    fieldWithPath("pageInfo.totalPage").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                    fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 데이터 수"),
                                    fieldWithPath("pageInfo.pageSize").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                                    fieldWithPath("pageInfo.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                    fieldWithPath("pageInfo.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                    fieldWithPath("pageInfo.currentElements").type(JsonFieldType.NUMBER).description("현재 페이지 데이터 수")

                            )

                    )
            ));
        }


        @Test
        @DisplayName("요청 검색")
        @WithMockUser
        public void searchRequests() throws Exception {
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime modifiedAt = LocalDateTime.now();
            List<RequestComment> requestComments = new ArrayList<>();
            RequestDto.Response response1 =
                    new RequestDto.Response(1L, "talkUrl1",
                            "title1",
                            "content1",
                            "bookTitle1",
                            "author1",
                            "publisher1",
                            "thumbnail1",
                            "displayName1",
                            0L,
                            "imgUrl1",
                            null,
                            createdAt,
                            modifiedAt);
            RequestDto.Response response2 =
                    new RequestDto.Response(2L, "talkUrl2",
                            "title2",
                            "content2",
                            "bookTitle2",
                            "author2",
                            "publisher2",
                            "thumbnail2",
                            "displayName2",
                            0L,
                            "imgUrl2",
                            null,
                            createdAt,
                            modifiedAt);

            List<RequestDto.Response> responseList = new ArrayList<>();
            responseList.add(response1);
            responseList.add(response2);

            List<Request> requestList = new ArrayList<>();
            requestList.add(new Request());
            requestList.add(new Request());

            given(requestService.searchRequests(Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class)))
                    .willReturn(
                            new PageImpl<>(requestList,
                                    PageRequest.of(0, 10,
                                            Sort.by("createdAt").descending()
                                    ), 2)
                    );

            given(requestMapper.requestsToRequestResponseDtos(Mockito.anyList())).willReturn(responseList);

            ResultActions actions =
                    mockMvc.perform(get(BASE_URL + "/search?keyword=con&field=content")
                            .param("page", "2")
                            .param("size", "1")
                            .param("sort", "createdAt,desc") // <-- no space after comma!!!
                            .accept(MediaType.APPLICATION_JSON)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                    );

            actions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].title").value(responseList.get(0).getTitle()))
                    .andDo(document("search-requests",
                            getRequestPreProcessor(),
                            getResponsePreProcessor(),
                            requestParameters(
                                    parameterWithName("keyword").description("검색어"),
                                    parameterWithName("field").description("검색 대상[displayName, title, content, bookTitle, author, publisher]"),
                                    parameterWithName("page").description("페이지 번호"),
                                    parameterWithName("size").description("페이지 사이즈"),
                                    parameterWithName("sort").description("정렬 기준[createdAt,desc]"),
                                    parameterWithName("_csrf").description("csrf")
                            ),
                            // response body
                            responseFields(
                                    List.of(
                                            fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                            fieldWithPath("data.[].requestId").type(JsonFieldType.NUMBER).description("요청 식별자"),
                                            fieldWithPath("data.[].talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                            fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("게시글 제목"),
                                            fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("게시글 본문"),
                                            fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                            fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("저자"),
                                            fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                            fieldWithPath("data.[].thumbnail").type(JsonFieldType.STRING).description("책 이미지"),
                                            fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                            fieldWithPath("data.[].views").type(JsonFieldType.NUMBER).description("요청 조회수"),
                                            fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지"),
                                            fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("요청 생성 일자"),
                                            fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("요청 수정 일자"),
                                            fieldWithPath("data.[].requestComments").type(JsonFieldType.NULL).description("댓글 정보"),
                                            fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                            fieldWithPath("pageInfo.currentPage").type(JsonFieldType.NUMBER).description("페이지 번호"),
                                            fieldWithPath("pageInfo.totalPage").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                            fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("총 데이터 수"),
                                            fieldWithPath("pageInfo.pageSize").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
                                            fieldWithPath("pageInfo.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                                            fieldWithPath("pageInfo.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                            fieldWithPath("pageInfo.currentElements").type(JsonFieldType.NUMBER).description("현재 페이지 데이터 수")

                                    )

                            )
                    ));

    }

    @Test
    @WithMockUser
    @DisplayName("요청 삭제")
    public void deleteRequestTest() throws Exception {
        long requestId = 1L;
        doNothing().when(requestService).deleteRequest(Mockito.anyLong(), Mockito.anyString());

        ResultActions actions = mockMvc.perform(
                delete(BASE_URL + "/{request-id}", requestId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .headers(GenerateMockToken.getMockHeaderToken()));

        actions.andExpect(status().isNoContent())
                .andDo(document(
                                "delete-request",
                                requestHeaders(
                                        headerWithName("Authorization").description("Bearer Token")
                                ),
                                pathParameters(
                                        parameterWithName("request-id").description("요청 식별자"))
                        )
                );

    }

}
