package com.book.village.server.statistics.communityView;

import com.book.village.server.domain.community.controller.CommunityController;
import com.book.village.server.domain.community.dto.CommunityDto;
import com.book.village.server.domain.community.entity.Community;
import com.book.village.server.domain.community.mapper.CommunityMapper;
import com.book.village.server.domain.community.service.CommunityService;
import com.book.village.server.domain.member.entity.Member;
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

import static com.book.village.server.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommunityController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class communityViewRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityService communityService;

    @MockBean
    private CommunityMapper mapper;

    private static final String url = "/v1/communities";

    @Test
    @DisplayName("모든 커뮤니티 조회수로 정렬")
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
                                .param("sort", "view,desc")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("statistics-communities-view",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준[view,desc]"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].communityId").type(JsonFieldType.NUMBER).description("커뮤니티 식별자"),
                                        fieldWithPath("data.[].type").type(JsonFieldType.STRING).description("카테고리[notice, free, study, recommend]"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("제목"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("작성자"),
                                        fieldWithPath("data.[].view").type(JsonFieldType.NUMBER).description("조회수"),
                                        fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("작성자 프로필 이미지"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("생성 일자"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("수정 일자"),
                                        fieldWithPath("data.[].communityComments").type(JsonFieldType.NULL).description("댓글 정보"),
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

}
