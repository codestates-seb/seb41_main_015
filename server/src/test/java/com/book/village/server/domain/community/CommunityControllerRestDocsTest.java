package com.book.village.server.domain.community;

import com.book.village.server.domain.community.controller.CommunityController;
import com.book.village.server.domain.community.dto.CommunityDto;
import com.book.village.server.domain.community.entity.Community;
import com.book.village.server.domain.community.mapper.CommunityMapper;
import com.book.village.server.domain.community.service.CommunityService;
import com.book.village.server.domain.community_comment.dto.CommunityCommentDto;
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
import org.springframework.data.domain.*;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class CommunityControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityService communityService;

    @MockBean
    private CommunityMapper mapper;

    @Autowired
    private Gson gson;

    private static final String url = "/v1/communities";

    @Test
    @DisplayName("???????????? ??????")
    @WithMockUser
    public void postCommunityTest() throws Exception {
        long communityId=1L;
        CommunityDto.Post post = new CommunityDto.Post(
                "free",
                "title1",
                "content1",
                "displayName1"
        );

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;
        String content = gson.toJson(post);

        List<CommunityCommentDto.Response> cCommentResponse=List.of(new CommunityCommentDto.Response(
                1L, "content1", "displayName1","imgUrl1",createdAt, createdAt),
                new CommunityCommentDto.Response(2L, "content2", "displayName2","imgUrl2",createdAt, createdAt)
        );

        CommunityDto.Response response = new CommunityDto.Response(
                communityId,
                "free",
                "title1",
                "content1",
                "displayName1",
                0L,
                "imgUrl1",
                cCommentResponse,
                createdAt,
                modifiedAt
        );

        given(mapper.postCommunityDtoToCommunity(Mockito.any(CommunityDto.Post.class))).willReturn(new Community());
        given(communityService.createCommunity(Mockito.any(Community.class),Mockito.anyString())).willReturn(new Community());

        given(mapper.communityToCommunityResponseDto(Mockito.any(Community.class))).willReturn(response);


        ResultActions actions =
                mockMvc.perform(
                        post(url)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.type").value(post.getType()))
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.displayName").value(post.getDisplayName()))
                .andDo(document("post-community",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("type").type(JsonFieldType.STRING).description("????????????[notice, free, study, recommend]"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("?????????").ignored()
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.communityId").type(JsonFieldType.NUMBER).description("???????????? ?????????"),
                                        fieldWithPath("data.type").type(JsonFieldType.STRING).description("????????????[notice, free, study, recommend]"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("???????????? ??????"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("???????????? ??????"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("???????????? ?????????"),
                                        fieldWithPath("data.view").type(JsonFieldType.NUMBER).description("???????????? ?????????"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("???????????? ????????? ????????? ?????????"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("???????????? ?????? ??????"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("???????????? ?????? ??????"),
                                        fieldWithPath("data.communityComments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("data.communityComments.[].communityCommentId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.communityComments.[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.communityComments.[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("data.communityComments.[].imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ?????????"),
                                        fieldWithPath("data.communityComments.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("data.communityComments.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("???????????? ??????")
    @WithMockUser
    public void patchCommunityTest() throws Exception {
        long communityId=1L;
        CommunityDto.Patch patch = new CommunityDto.Patch(
                communityId,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L
        );

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;
        String content = gson.toJson(patch);

        List<CommunityCommentDto.Response> cCommentResponse=List.of(new CommunityCommentDto.Response(
                        1L, "content1", "displayName1","imgUrl1",createdAt, createdAt),
                new CommunityCommentDto.Response(2L, "content2", "displayName2","imgUrl2",createdAt, createdAt)
        );

        CommunityDto.Response response = new CommunityDto.Response(
                communityId,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L,
                "imgUrl1",
                cCommentResponse,
                createdAt,
                modifiedAt
        );

        given(mapper.patchCommunityDtoToCommunity(Mockito.any(CommunityDto.Patch.class))).willReturn(new Community());
        given(communityService.updateCommunity(Mockito.any(Community.class),Mockito.anyString())).willReturn(new Community());

        given(mapper.communityToCommunityResponseDto(Mockito.any(Community.class))).willReturn(response);


        ResultActions actions =
                mockMvc.perform(
                        patch(url+"/{community-id}",communityId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.communityId").value(patch.getCommunityId()))
                .andExpect(jsonPath("$.data.type").value(patch.getType()))
                .andExpect(jsonPath("$.data.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andExpect(jsonPath("$.data.displayName").value(patch.getDisplayName()))
                .andDo(document("patch-community",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("community-id").description("???????????? ?????????")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("communityId").type(JsonFieldType.STRING).description("???????????? ?????????").ignored(),
                                        fieldWithPath("type").type(JsonFieldType.STRING).description("????????????[notice, free, study, recommend]").optional(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("??????").optional(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("??????").optional(),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("?????????").optional(),
                                        fieldWithPath("view").type(JsonFieldType.NUMBER).description("?????????").ignored()
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.communityId").type(JsonFieldType.NUMBER).description("???????????? ?????????"),
                                        fieldWithPath("data.type").type(JsonFieldType.STRING).description("????????????[notice, free, study, recommend]"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("data.view").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("????????? ????????? ?????????"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.communityComments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("data.communityComments.[].communityCommentId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.communityComments.[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.communityComments.[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("data.communityComments.[].imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ?????????"),
                                        fieldWithPath("data.communityComments.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("data.communityComments.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("???????????? ??????")
    @WithMockUser
    public void getCommunityTest() throws Exception {
        long communityId=1L;

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;

        List<CommunityCommentDto.Response> cCommentResponse=List.of(new CommunityCommentDto.Response(
                        1L, "content1", "displayName1","imgUrl1",createdAt, createdAt),
                new CommunityCommentDto.Response(2L, "content2", "displayName2","imgUrl2",createdAt, createdAt)
        );

        CommunityDto.Response response = new CommunityDto.Response(
                communityId,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L,
                "imgUrl1",
                cCommentResponse,
                createdAt,
                modifiedAt
        );
        Community community =new Community(
                1L,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L,
                new Member("email@gmail.com"),
                null
        );

        given(communityService.findCommunity(Mockito.anyLong())).willReturn(community);
        given(communityService.updateCommunity(Mockito.any(Community.class),Mockito.anyString())).willReturn(community);
        given(mapper.communityToCommunityResponseDto(Mockito.any(Community.class))).willReturn(response);


        ResultActions actions =
                mockMvc.perform(
                        get(url+"/{community-id}",communityId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.communityId").value(response.getCommunityId()))
                .andExpect(jsonPath("$.data.type").value(response.getType()))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.content").value(response.getContent()))
                .andExpect(jsonPath("$.data.displayName").value(response.getDisplayName()))
                .andExpect(jsonPath("$.data.imgUrl").value(response.getImgUrl()))
                .andDo(document("get-community",
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("community-id").description("???????????? ?????????")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ?????????"),
                                        fieldWithPath("data.communityId").type(JsonFieldType.NUMBER).description("???????????? ?????????"),
                                        fieldWithPath("data.type").type(JsonFieldType.STRING).description("????????????[notice, free, study, recommend]"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("data.view").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("????????? ????????? ?????????"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.communityComments").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("data.communityComments.[].communityCommentId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("data.communityComments.[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.communityComments.[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("data.communityComments.[].imgUrl").type(JsonFieldType.STRING).description("?????? ????????? ????????? ?????????"),
                                        fieldWithPath("data.communityComments.[].createdAt").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("data.communityComments.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ?????? ??????")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("?????? ???????????? ??????")
    @WithMockUser
    public void getCommunitiesTest() throws Exception {

        LocalDateTime createdAt1=LocalDateTime.now();
        LocalDateTime createdAt2=createdAt1.minusDays(1L);

        CommunityDto.Response response1 = new CommunityDto.Response(
                1L,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L,
                "imgUrl1",
                null,
                createdAt1,
                createdAt1
        );
        CommunityDto.Response response2 = new CommunityDto.Response(
                2L,
                "free",
                "title2",
                "content2",
                "displayName2",
                1L,
                "imgUrl2",
                null,
                createdAt2,
                createdAt2
        );

        List<CommunityDto.Response> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        List<Community> list = new ArrayList<>();
        list.add(new Community(1L,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L,
                new Member(),
                null));
        list.add(new Community(
                2L,
                "free",
                "title2",
                "content2",
                "displayName2",
                1L,
                new Member(),
                null));

        given(communityService.findCommunities(Mockito.any(Pageable.class))).willReturn(new PageImpl<>(
                list,
                PageRequest.of(0,10,
                        Sort.by("createdAt").descending()),2));
        given(mapper.communitiesToCommunityResponseDtos(Mockito.anyList())).willReturn(responseList);


        ResultActions actions =
                mockMvc.perform(
                        get(url)
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "createdAt,desc")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("get-communities",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ?????????"),
                                parameterWithName("sort").description("?????? ??????[createdAt,desc]"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                        fieldWithPath("data.[].communityId").type(JsonFieldType.NUMBER).description("???????????? ?????????"),
                                        fieldWithPath("data.[].type").type(JsonFieldType.STRING).description("????????????[notice, free, study, recommend]"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("????????? ????????? ?????????"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.[].communityComments").type(JsonFieldType.NULL).description("?????? ??????"),
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
    @DisplayName("?????? ?????? ???????????? ??????")
    @WithMockUser
    public void getMyCommunitiesTest() throws Exception {

        LocalDateTime createdAt1=LocalDateTime.now();
        LocalDateTime createdAt2=createdAt1.minusDays(1L);

        CommunityDto.Response response1 = new CommunityDto.Response(
                1L,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L,
                "imgUrl1",
                null,
                createdAt1,
                createdAt1
        );
        CommunityDto.Response response2 = new CommunityDto.Response(
                2L,
                "free",
                "title2",
                "content2",
                "displayName2",
                1L,
                "imgUrl2",
                null,
                createdAt2,
                createdAt2
        );

        List<CommunityDto.Response> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        List<Community> list = new ArrayList<>();
        list.add(new Community(1L,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L,
                new Member(),
                null));
        list.add(new Community(
                2L,
                "free",
                "title2",
                "content2",
                "displayName2",
                1L,
                new Member(),
                null));

        given(communityService.findMyCommunities(Mockito.anyString(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(
                list,
                PageRequest.of(0,5,
                        Sort.by("createdAt").descending()),2));
        given(mapper.communitiesToCommunityResponseDtos(Mockito.anyList())).willReturn(responseList);


        ResultActions actions =
                mockMvc.perform(
                        get(url+"/mine")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "questionId,desc")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("get-communities-mine",
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
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                        fieldWithPath("data.[].communityId").type(JsonFieldType.NUMBER).description("???????????? ?????????"),
                                        fieldWithPath("data.[].type").type(JsonFieldType.STRING).description("????????????[notice, free, study, recommend]"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("????????? ????????? ?????????"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.[].communityComments").type(JsonFieldType.NULL).description("?????? ??????"),
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
    @DisplayName("???????????? ??????")
    @WithMockUser
    public void deleteCommunityTest() throws Exception {
        long communityId=1L;

        doNothing().when(communityService).deleteCommunity(Mockito.anyLong(), Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        delete(url+"/{community-id}",communityId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-community",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("community-id").description("???????????? ?????????")
                        )
                ));
    }

    @Test
    @DisplayName("???????????? ??????")
    @WithMockUser
    public void searchCommunitiesTest() throws Exception {

        LocalDateTime createdAt1=LocalDateTime.now();
        LocalDateTime createdAt2=createdAt1.minusDays(1L);

        CommunityDto.Response response1 = new CommunityDto.Response(
                1L,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L,
                "imgUrl1",
                null,
                createdAt1,
                createdAt1
        );
        CommunityDto.Response response2 = new CommunityDto.Response(
                2L,
                "free",
                "title2",
                "content2",
                "displayName2",
                1L,
                "imgUrl2",
                null,
                createdAt2,
                createdAt2
        );

        List<CommunityDto.Response> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        List<Community> list = new ArrayList<>();
        list.add(new Community(1L,
                "free",
                "title1",
                "content1",
                "displayName1",
                1L,
                new Member(),
                null));
        list.add(new Community(
                2L,
                "free",
                "title2",
                "content2",
                "displayName2",
                1L,
                new Member(),
                null));

        given(communityService.searchCommunity(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(
                list,
                PageRequest.of(0,5,
                        Sort.by("createdAt").descending()),2));
        given(mapper.communitiesToCommunityResponseDtos(Mockito.anyList())).willReturn(responseList);


        ResultActions actions =
                mockMvc.perform(
                        get(url+"/search?field=title&keyword=this&type=study")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "createdAt,desc")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("search-communities",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("keyword").description("?????????"),
                                parameterWithName("field").description("?????? ??????[title, content, displayName]"),
                                parameterWithName("type").description("???????????? ????????????[notice, free, study, recommend]"),
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ?????????"),
                                parameterWithName("sort").description("?????? ??????[createdAt,desc]"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                        fieldWithPath("data.[].communityId").type(JsonFieldType.NUMBER).description("???????????? ?????????"),
                                        fieldWithPath("data.[].type").type(JsonFieldType.STRING).description("????????????[notice, free, study, recommend]"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("??????"),
                                        fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("?????????"),
                                        fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("?????????"),
                                        fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("????????? ????????? ?????????"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("data.[].communityComments").type(JsonFieldType.NULL).description("?????? ??????"),
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
}
