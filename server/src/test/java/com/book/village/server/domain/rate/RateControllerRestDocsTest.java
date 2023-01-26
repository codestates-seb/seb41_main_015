package com.book.village.server.domain.rate;

import com.book.village.server.domain.book.entity.Book;
import com.book.village.server.domain.member.entity.Member;
import com.book.village.server.domain.rate.controller.RateController;
import com.book.village.server.domain.rate.dto.RateDto;
import com.book.village.server.domain.rate.entity.Rate;
import com.book.village.server.domain.rate.mapper.RateMapper;
import com.book.village.server.domain.rate.service.RateService;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RateController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class RateControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RateService rateService;

    @MockBean
    private RateMapper mapper;

    @Autowired
    private Gson gson;

    private static final String url = "/v1/rates";

    @Test
    @DisplayName("평점 등록")
    @WithMockUser
    public void postRateTest() throws Exception {
        long rateId = 1L;

        RateDto.Post post = new RateDto.Post(
                3L,
                "content1",
                "thumbnail1"
        );
        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;
        String content = gson.toJson(post);


        RateDto.Response response = new RateDto.Response(
                rateId,
                3L,
                "displayName1",
                "imgUrl1",
                "content1",
                createdAt,
                createdAt
        );

        given(mapper.ratePostDtoToRate(Mockito.any(RateDto.Post.class))).willReturn(new Rate());
        given(rateService.createRate(Mockito.any(Rate.class),Mockito.anyString(),
                Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).willReturn(new Rate());
        given(mapper.rateToRateResponseDto(Mockito.any(Rate.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        post(url)
                                .param("isbn", "isbn")
                                .param("bookTitle", "bookTitle1")
                                .param("author", "author1")
                                .param("publisher", "publisher1")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.rating").value(post.getRating()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andDo(document("post-rate",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestParameters(
                                parameterWithName("isbn").description("isbn"),
                                parameterWithName("bookTitle").description("도서 제목"),
                                parameterWithName("author").description("저자"),
                                parameterWithName("publisher").description("출판사"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("rating").type(JsonFieldType.NUMBER).description("평점"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("평점 내용"),
                                        fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("책 표지 이미지")
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.rateId").type(JsonFieldType.NUMBER).description("평점 식별자"),
                                        fieldWithPath("data.rating").type(JsonFieldType.NUMBER).description("평점"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("작성자"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("작성자 프로필 이미지"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("평점 내용"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("평점 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("평점 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("평점 수정")
    @WithMockUser
    public void patchRateTest() throws Exception {
        long rateId = 1L;

        RateDto.Patch patch = new RateDto.Patch(
                rateId,
                3L,
                "displayName1",
                "content1"
        );
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = createdAt;
        String content = gson.toJson(patch);


        RateDto.Response response = new RateDto.Response(
                rateId,
                3L,
                "displayName1",
                "imgUrl1",
                "content1",
                createdAt,
                createdAt
        );

        given(mapper.ratePatchDtoToRate(Mockito.any(RateDto.Patch.class))).willReturn(new Rate());
        given(rateService.updateRate(Mockito.any(Rate.class), Mockito.anyString())).willReturn(new Rate());
        given(mapper.rateToRateResponseDto(Mockito.any(Rate.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        patch(url + "/{rate-id}", rateId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.rating").value(patch.getRating()))
                .andExpect(jsonPath("$.data.displayName").value(patch.getDisplayName()))
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andDo(document("patch-rate",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("rate-id").description("평점 식별자")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("rateId").type(JsonFieldType.NUMBER).description("평점 식별자").ignored(),
                                        fieldWithPath("rating").type(JsonFieldType.NUMBER).description("평점").optional(),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("평점 작성자").optional(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("평점 내용").optional()
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.rateId").type(JsonFieldType.NUMBER).description("평점 식별자"),
                                        fieldWithPath("data.rating").type(JsonFieldType.NUMBER).description("평점"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("작성자"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("작성자 프로필 이미지"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("평점 내용"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("평점 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("평점 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("평점 조회")
    @WithMockUser
    public void getRateTest() throws Exception {
        long rateId = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = createdAt;


        RateDto.Response response = new RateDto.Response(
                rateId,
                3L,
                "displayName1",
                "imgUrl",
                "content1",
                createdAt,
                createdAt
        );

        given(rateService.findRate(Mockito.anyLong())).willReturn(new Rate());
        given(mapper.rateToRateResponseDto(Mockito.any(Rate.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        get(url + "/{rate-id}", rateId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("get-rate",
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("rate-id").description("평점 식별자")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.rateId").type(JsonFieldType.NUMBER).description("평점 식별자"),
                                        fieldWithPath("data.rating").type(JsonFieldType.NUMBER).description("평점"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("작성자"),
                                        fieldWithPath("data.imgUrl").type(JsonFieldType.STRING).description("작성자 프로필 이미지"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("평점 내용"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("평점 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("평점 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("나의 모든 평점 조회")
    @WithMockUser
    public void getMyRatesTest() throws Exception {
        long rateId = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime modifiedAt = createdAt;


        RateDto.Response response1 = new RateDto.Response(
                rateId,
                3L,
                "displayName1",
                "imgUrl1",
                "content1",
                createdAt,
                createdAt
        );

        RateDto.Response response2 = new RateDto.Response(
                rateId,
                3L,
                "displayName2",
                "imgUrl2",
                "content2",
                createdAt,
                createdAt
        );
        List<RateDto.Response> responseList =new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        List<Rate> list = List.of(
                new Rate(1L, 3L, "displayName1", "content1", new Book(), new Member()),
                new Rate(2L, 3L, "displayName2", "content2", new Book(), new Member())
        );

        given(rateService.findMyRates(Mockito.anyString(),Mockito.any(Pageable.class))).willReturn(
                new PageImpl<>(list,
                        PageRequest.of(0,10,
                                Sort.by("createdAt").descending()),2)
        );
        given(mapper.ratesToRateResponseDtos(Mockito.anyList())).willReturn(responseList);

        ResultActions actions =
                mockMvc.perform(
                        get(url+"/mine")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "createdAt,desc")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("get-rates-mine",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준[createdAt,desc]"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].rateId").type(JsonFieldType.NUMBER).description("평점 식별자"),
                                        fieldWithPath("data.[].rating").type(JsonFieldType.NUMBER).description("평점"),
                                        fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("작성자"),
                                        fieldWithPath("data.[].imgUrl").type(JsonFieldType.STRING).description("작성자 프로필 이미지"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("평점 내용"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("평점 생성 일자"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("평점 수정 일자"),
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
    @DisplayName("평점 삭제")
    @WithMockUser
    public void deleteRateTest() throws Exception {
        long rateId = 1L;

        doNothing().when(rateService).deleteRate(Mockito.anyLong(),Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        delete(url + "/{rate-id}", rateId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-rate",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("rate-id").description("평점 식별자")
                        )
                ));
    }
}
