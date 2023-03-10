package com.book.village.server.domain.request;

import com.book.village.server.domain.member.entity.Member;
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
    @DisplayName("?????? ????????? ??????")
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
                                        fieldWithPath("talkUrl").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("??? ??????"),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("??? ?????????")
                                )

                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.requestId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("??? ??????"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("??? ?????????"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("data.view").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("data.requestComments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("data.requestComments.[].requestCommentId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.requestComments.[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.requestComments.[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("data.requestComments.[].imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ?????????"),
                                        fieldWithPath("data.requestComments.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("data.requestComments.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????")
                                )
                        )
                ));


    }

    @Test
    @DisplayName("?????? ????????? ??????")
    @WithMockUser
    public void patchRequestTest() throws Exception {
        long requestId = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = LocalDateTime.now();
        RequestDto.Patch patch = new RequestDto.Patch(
                requestId,
                "talkUrl",
                "title",
                0L,
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
                .andExpect(jsonPath("$.data.view").value(patch.getView()))
                .andDo(document("patch-request",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("request-id").description("?????? ?????????")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("requestId").type(JsonFieldType.NUMBER).description("?????? ?????????").ignored(),
                                        fieldWithPath("talkUrl").type(JsonFieldType.STRING).description("????????? ??????").optional(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("????????? ??????").optional(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("????????? ??????").optional(),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("??? ??????").optional(),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("??????").optional(),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("?????????").optional(),
                                        fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("??? ?????????").optional(),
                                        fieldWithPath("view").type(JsonFieldType.NUMBER).description("?????????").ignored()
                                )

                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.requestId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("??? ??????"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("??? ????????? ?????????"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("data.view").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("data.requestComments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("data.requestComments.[].requestCommentId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.requestComments.[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.requestComments.[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("data.requestComments.[].imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ?????????"),
                                        fieldWithPath("data.requestComments.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("data.requestComments.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????")
                                )
                        )
                ));
    }


        @Test
        @DisplayName("?????? ??????")
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
            Request request= new Request(
                    1L,
                    "talkUrl",
                    "title",
                    "content",
                    "bookTitle",
                    "author",
                    "publisher",
                    "thumbnail",
                    "displayName",
                    1L,
                    new Member("test@gmail.com"),
                    null
            );

            given(requestService.findRequest(Mockito.anyLong())).willReturn(request);
            given(requestService.updateRequest(Mockito.any(Request.class),Mockito.anyString())).willReturn(request);
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
                                    parameterWithName("request-id").description("?????? ?????????")
                            ),
                            responseFields(
                                    List.of(
                                            fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                            fieldWithPath("data.requestId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                            fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("????????? ??????"),
                                            fieldWithPath("data.title").type(JsonFieldType.STRING).description("????????? ??????"),
                                            fieldWithPath("data.content").type(JsonFieldType.STRING).description("????????? ??????"),
                                            fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("??? ??????"),
                                            fieldWithPath("data.author").type(JsonFieldType.STRING).description("??????"),
                                            fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("?????????"),
                                            fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                            fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("??? ????????? ?????????"),
                                            fieldWithPath("data.view").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                            fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                            fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                            fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                            fieldWithPath("data.requestComments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                            fieldWithPath("data.requestComments.[].requestCommentId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                            fieldWithPath("data.requestComments.[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                                            fieldWithPath("data.requestComments.[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                            fieldWithPath("data.requestComments.[].imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                            fieldWithPath("data.requestComments.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                            fieldWithPath("data.requestComments.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????")
                                    )
                            )
                    ));
        }

        @Test
        @DisplayName("??? ?????? ?????? ??????")
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
                                    parameterWithName("page").description("????????? ??????"),
                                    parameterWithName("size").description("????????? ?????????"),
                                    parameterWithName("sort").description("?????? ??????[createdAt,desc]"),
                                    parameterWithName("_csrf").description("csrf")
                            ),
                            responseFields(
                                    List.of(
                                            fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                            fieldWithPath("data.[].requestId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                            fieldWithPath("data.[].talkUrl").type(JsonFieldType.STRING).description("????????? ??????"),
                                            fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                                            fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("????????? ??????"),
                                            fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("??? ??????"),
                                            fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("??????"),
                                            fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("?????????"),
                                            fieldWithPath("data.[].thumbnail").type(JsonFieldType.STRING).description("??? ?????????"),
                                            fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                            fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                            fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                            fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                            fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                            fieldWithPath("data.[].requestComments").type(JsonFieldType.NULL).description("?????? ??????"),
                                            fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("????????? ??????"),
                                            fieldWithPath("pageInfo.currentPage").type(JsonFieldType.NUMBER).description("????????? ??????"),
                                            fieldWithPath("pageInfo.totalPage").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
                                            fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
                                            fieldWithPath("pageInfo.pageSize").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                            fieldWithPath("pageInfo.first").type(JsonFieldType.BOOLEAN).description("??? ????????? ??????"),
                                            fieldWithPath("pageInfo.last").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????"),
                                            fieldWithPath("pageInfo.currentElements").type(JsonFieldType.NUMBER).description("?????? ????????? ????????? ???")
                                    )
                            )));
        }


        @Test
        @DisplayName("?????? ?????? ??????")
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
                            parameterWithName("page").description("????????? ??????"),
                            parameterWithName("size").description("????????? ?????????"),
                            parameterWithName("sort").description("?????? ??????[createdAt,desc]")
                    ),
                    // response body
                    responseFields(
                            List.of(
                                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                    fieldWithPath("data.[].requestId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                    fieldWithPath("data.[].talkUrl").type(JsonFieldType.STRING).description("????????? ??????"),
                                    fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                                    fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("????????? ??????"),
                                    fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("??? ??????"),
                                    fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("??????"),
                                    fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("?????????"),
                                    fieldWithPath("data.[].thumbnail").type(JsonFieldType.STRING).description("??? ?????????"),
                                    fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                    fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("?????? ?????????"),
                                    fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                    fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                    fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                    fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                    fieldWithPath("data.[].requestComments").type(JsonFieldType.NULL).description("?????? ??????"),
                                    fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("????????? ??????"),
                                    fieldWithPath("pageInfo.currentPage").type(JsonFieldType.NUMBER).description("????????? ??????"),
                                    fieldWithPath("pageInfo.totalPage").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
                                    fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
                                    fieldWithPath("pageInfo.pageSize").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                    fieldWithPath("pageInfo.first").type(JsonFieldType.BOOLEAN).description("??? ????????? ??????"),
                                    fieldWithPath("pageInfo.last").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????"),
                                    fieldWithPath("pageInfo.currentElements").type(JsonFieldType.NUMBER).description("?????? ????????? ????????? ???")

                            )

                    )
            ));
        }


        @Test
        @DisplayName("?????? ??????")
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
                                    parameterWithName("keyword").description("?????????"),
                                    parameterWithName("field").description("?????? ??????[displayName, title, content, bookTitle, author, publisher]"),
                                    parameterWithName("page").description("????????? ??????"),
                                    parameterWithName("size").description("????????? ?????????"),
                                    parameterWithName("sort").description("?????? ??????[createdAt,desc]"),
                                    parameterWithName("_csrf").description("csrf")
                            ),
                            // response body
                            responseFields(
                                    List.of(
                                            fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                            fieldWithPath("data.[].requestId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                            fieldWithPath("data.[].talkUrl").type(JsonFieldType.STRING).description("????????? ??????"),
                                            fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                                            fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("????????? ??????"),
                                            fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("??? ??????"),
                                            fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("??????"),
                                            fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("?????????"),
                                            fieldWithPath("data.[].thumbnail").type(JsonFieldType.STRING).description("??? ?????????"),
                                            fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                            fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                            fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                                            fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                            fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                            fieldWithPath("data.[].requestComments").type(JsonFieldType.NULL).description("?????? ??????"),
                                            fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("????????? ??????"),
                                            fieldWithPath("pageInfo.currentPage").type(JsonFieldType.NUMBER).description("????????? ??????"),
                                            fieldWithPath("pageInfo.totalPage").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
                                            fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
                                            fieldWithPath("pageInfo.pageSize").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                            fieldWithPath("pageInfo.first").type(JsonFieldType.BOOLEAN).description("??? ????????? ??????"),
                                            fieldWithPath("pageInfo.last").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????"),
                                            fieldWithPath("pageInfo.currentElements").type(JsonFieldType.NUMBER).description("?????? ????????? ????????? ???")

                                    )

                            )
                    ));

    }

    @Test
    @WithMockUser
    @DisplayName("?????? ??????")
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
                                        parameterWithName("request-id").description("?????? ?????????"))
                        )
                );

    }

}
