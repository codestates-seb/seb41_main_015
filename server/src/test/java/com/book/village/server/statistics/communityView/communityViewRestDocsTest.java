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
    @DisplayName("?????? ???????????? ???????????? ??????")
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
                                parameterWithName("page").description("????????? ??????"),
                                parameterWithName("size").description("????????? ?????????"),
                                parameterWithName("sort").description("?????? ??????[view,desc]"),
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
