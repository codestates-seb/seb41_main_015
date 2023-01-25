package com.book.village.server.domain.community_comment;

import com.book.village.server.domain.community.entity.Community;
import com.book.village.server.domain.community_comment.controller.CommunityCommentController;
import com.book.village.server.domain.community_comment.dto.CommunityCommentDto;
import com.book.village.server.domain.community_comment.entity.CommunityComment;
import com.book.village.server.domain.community_comment.mapper.CommunityCommentMapper;
import com.book.village.server.domain.community_comment.service.CommunityCommentService;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityCommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class CommunityCommentControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityCommentService commentService;

    @MockBean
    private CommunityCommentMapper mapper;

    @Autowired
    private Gson gson;

    private static final String url = "/v1/communities/comments";

    @Test
    @DisplayName("커뮤니티 댓글 등록")
    @WithMockUser
    public void postCommunityComment() throws Exception{
        long cCommentId= 1L;
        long communityId=1L;
        CommunityCommentDto.Post post = new CommunityCommentDto.Post(
                "content1"
        );
        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;
        String content = gson.toJson(post);

        CommunityCommentDto.Response response= new CommunityCommentDto.Response(
                cCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                createdAt
        );
        given(mapper.communityCommentPostDtoToCommunityComment(Mockito.any(CommunityCommentDto.Post.class))).willReturn(new CommunityComment());
        given(commentService.createCommunityComment(Mockito.any(CommunityComment.class), Mockito.anyString(), Mockito.anyLong())).willReturn(new CommunityComment());
        given(mapper.communityCommentToCommunityCommentResponseDto(Mockito.any(CommunityComment.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        post(url+"/{community-id}", communityId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andDo(document("post-community-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("community-id").description("커뮤니티 식별자")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.communityCommentId").type(JsonFieldType.NUMBER).description("커뮤니티 댓글 식별자"),
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
    @DisplayName("커뮤니티 댓글 수정")
    @WithMockUser
    public void patchCommunityComment() throws Exception{
        long cCommentId= 1L;
        CommunityCommentDto.Patch patch = new CommunityCommentDto.Patch(
                cCommentId,
                "content1"
        );
        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;
        String content = gson.toJson(patch);

        CommunityCommentDto.Response response= new CommunityCommentDto.Response(
                cCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                createdAt
        );
        given(mapper.communityCommentPatchDtoToCommunityComment(Mockito.any(CommunityCommentDto.Patch.class))).willReturn(new CommunityComment());
        given(commentService.updateCommunityComment(Mockito.any(CommunityComment.class), Mockito.anyString())).willReturn(new CommunityComment());
        given(mapper.communityCommentToCommunityCommentResponseDto(Mockito.any(CommunityComment.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        patch(url+"/{communityComment-id}", cCommentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andDo(document("patch-community-comment",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("communityComment-id").description("커뮤니티 댓글 식별자")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("communityCommentId").type(JsonFieldType.STRING).description("커뮤니티 댓글 식별자").ignored(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용").optional()
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.communityCommentId").type(JsonFieldType.NUMBER).description("커뮤니티 댓글 식별자"),
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
    @DisplayName("커뮤니티 댓글 조회")
    @WithMockUser
    public void getCommunityComment() throws Exception{
        long cCommentId= 1L;

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;

        CommunityCommentDto.Response response= new CommunityCommentDto.Response(
                cCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                createdAt
        );

        given(commentService.findCommunityComment(Mockito.anyLong())).willReturn(new CommunityComment());
        given(mapper.communityCommentToCommunityCommentResponseDto(Mockito.any(CommunityComment.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        get(url+"/{communityComment-id}", cCommentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("get-community-comment",
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("communityComment-id").description("커뮤니티 댓글 식별자")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.communityCommentId").type(JsonFieldType.NUMBER).description("커뮤니티 댓글 식별자"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("댓글 작성자 이미지 프로필"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("나의 모든 커뮤니티 댓글 조회")
    @WithMockUser
    public void getMyCommunityComments() throws Exception{
        LocalDateTime createdAt1=LocalDateTime.now();
        LocalDateTime createdAt2=createdAt1.minusDays(1L);

        CommunityCommentDto.Response response1= new CommunityCommentDto.Response(
                1L,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt1,
                createdAt1
        );

        CommunityCommentDto.Response response2= new CommunityCommentDto.Response(
                2L,
                "content2",
                "displayName2",
                "imgUrl2",
                createdAt2,
                createdAt2
        );
        List<CommunityCommentDto.Response> responseList=new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        List<CommunityComment> communityCommentList=new ArrayList<>();
        communityCommentList.add(new CommunityComment(
                1L,
                "content1",
                "displayName1",
                new Community(),
                new Member()
        ));

        communityCommentList.add(new CommunityComment(
                2L,
                "content2",
                "displayName2",
                new Community(),
                new Member()
        ));

        given(commentService.findMyCommunityComments(Mockito.anyString())).willReturn(communityCommentList);
        given(mapper.communityCommentsToCommunityCommentResponseDtos(Mockito.anyList())).willReturn(responseList);

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
                .andDo(document("get-community-comments-mine",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].communityCommentId").type(JsonFieldType.NUMBER).description("커뮤니티 댓글 식별자"),
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
    public void deleteCommunityComments() throws Exception{

        long cCommentId= 1L;

        LocalDateTime createdAt=LocalDateTime.now();

        CommunityCommentDto.Response response= new CommunityCommentDto.Response(
                cCommentId,
                "content1",
                "displayName1",
                "imgUrl1",
                createdAt,
                createdAt
        );

        doNothing().when(commentService).deleteCommunityComment(Mockito.anyLong(),Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        delete(url+"/{communityComment-id}", cCommentId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-community-comment",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("communityComment-id").description("커뮤니티 댓글 식별자")
                        )
                ));
    }
}
