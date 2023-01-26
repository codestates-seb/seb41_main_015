package com.book.village.server.domain.request_comment;

import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.request.entity.Request;
import com.book.village.server.domain.request_comment.controller.RequestCommentController;
import com.book.village.server.domain.request_comment.dto.RequestCommentDto;
import com.book.village.server.domain.request_comment.entity.RequestComment;
import com.book.village.server.domain.request_comment.mapper.RequestCommentMapper;
import com.book.village.server.domain.request_comment.service.RequestCommentService;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestCommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class RequestCommentControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestCommentService commentService;

    @MockBean
    private RequestCommentMapper mapper;

    @Autowired
    private Gson gson;

    private static final String url = "/v1/requests/comments";

    @Test
    @DisplayName("요청 댓글 등록")
    @WithMockUser
    public void postRequestComment() throws Exception{
        long requestCommentId= 1L;
        long requestId=1L;
        RequestCommentDto.Post post = new RequestCommentDto.Post(
                "content1"
        );
        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=LocalDateTime.now();
        String content = gson.toJson(post);

        RequestCommentDto.Response response= new RequestCommentDto.Response(
                requestCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                modifiedAt
        );
        given(mapper.requestCommentPostDtoToRequestComment(Mockito.any(RequestCommentDto.Post.class))).willReturn(new RequestComment());
        given(commentService.createRequestComment(Mockito.any(RequestComment.class), Mockito.anyString(), Mockito.anyLong())).willReturn(new RequestComment());
        given(mapper.requestCommentToRequestCommentResponseDto(Mockito.any(RequestComment.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        post(url+"/{request-id}", requestId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andDo(document("post-request-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("request-id").description("요청 식별자")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.requestCommentId").type(JsonFieldType.NUMBER).description("요청 댓글 식별자"),
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
    @DisplayName("요청 댓글 수정")
    @WithMockUser
    public void patchRequestComment() throws Exception{
        long requestCommentId= 1L;
        RequestCommentDto.Patch post = new RequestCommentDto.Patch(
                requestCommentId,
                "content1"
        );
        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=LocalDateTime.now();
        String content = gson.toJson(post);

        RequestCommentDto.Response response= new RequestCommentDto.Response(
                requestCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                createdAt
        );
        given(mapper.requestCommentPatchDtoToRequestComment(Mockito.any(RequestCommentDto.Patch.class))).willReturn(new RequestComment());
        given(commentService.updateRequestComment(Mockito.any(RequestComment.class), Mockito.anyString())).willReturn(new RequestComment());
        given(mapper.requestCommentToRequestCommentResponseDto(Mockito.any(RequestComment.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        patch(url+"/{requestComment-id}", requestCommentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andDo(document("patch-request-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("requestComment-id").description("요청 댓글 식별자")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("requestCommentId").type(JsonFieldType.STRING).description("요청 댓글 식별자").ignored(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용").optional()
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.requestCommentId").type(JsonFieldType.NUMBER).description("요청 댓글 식별자"),
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
    @DisplayName("요청 댓글 조회")
    @WithMockUser
    public void getRequestComment() throws Exception{
        long requestCommentId= 1L;

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=LocalDateTime.now();

        RequestCommentDto.Response response= new RequestCommentDto.Response(
                requestCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                createdAt
        );

        given(commentService.findRequestComment(Mockito.anyLong())).willReturn(new RequestComment());
        given(mapper.requestCommentToRequestCommentResponseDto(Mockito.any(RequestComment.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        get(url+"/{requestComment-id}", requestCommentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("get-request-comment",
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("requestComment-id").description("요청 댓글 식별자")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.requestCommentId").type(JsonFieldType.NUMBER).description("요청 댓글 식별자"),
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
    @DisplayName("나의 모든 요청 댓글 조회")
    @WithMockUser
    public void getMyRequestComments() throws Exception{
        LocalDateTime createdAt1=LocalDateTime.now();
        LocalDateTime createdAt2=createdAt1.minusDays(1L);

        RequestCommentDto.Response response1= new RequestCommentDto.Response(
                1L,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt1,
                createdAt1
        );

        RequestCommentDto.Response response2= new RequestCommentDto.Response(
                2L,
                "content2",
                "displayName2",
                "imgUrl2",
                createdAt2,
                createdAt2
        );
        List<RequestCommentDto.Response> responseList=new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        List<RequestComment> requestCommentList=new ArrayList<>();
        requestCommentList.add(new RequestComment(
                1L,
                "content1",
                "displayName1",
                new Member(),
                new Request()
        ));

        requestCommentList.add(new RequestComment(
                2L,
                "content2",
                "displayName2",
                new Member(),
                new Request()
        ));

        given(commentService.findMyRequestComments(Mockito.anyString())).willReturn(requestCommentList);
        given(mapper.requestCommentsToRequestCommentResponseDtos(Mockito.anyList())).willReturn(responseList);

        ResultActions actions =
                mockMvc.perform(
                        get(url+"/mine")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())

                );
        actions
                .andExpect(status().isOk())
                .andDo(document("get-request-comments-mine",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].requestCommentId").type(JsonFieldType.NUMBER).description("요청 댓글 식별자"),
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
    @DisplayName("요청 댓글 삭제")
    @WithMockUser
    public void deleteRequestComments() throws Exception{

        long requestCommentId= 1L;

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=LocalDateTime.now();

        RequestCommentDto.Response response= new RequestCommentDto.Response(
                requestCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                modifiedAt
        );

        doNothing().when(commentService).deleteRequestComment(Mockito.anyLong(),Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        delete(url+"/{requestComment-id}", requestCommentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-request-comment",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("requestComment-id").description("요청 댓글 식별자")
                        )
                ));
    }
}