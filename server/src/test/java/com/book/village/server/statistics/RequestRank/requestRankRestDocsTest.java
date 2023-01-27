package com.book.village.server.statistics.RequestRank;

import com.book.village.server.domain.request.controller.RequestController;
import com.book.village.server.domain.request.dto.RequestDto;
import com.book.village.server.domain.request.mapper.RequestMapper;
import com.book.village.server.domain.request.service.RequestService;
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

@WebMvcTest(RequestController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class requestRankRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestMapper requestMapper; //사용 안하더라도 주석 남겨주기(의존성 주입)

    @MockBean
    private RequestService requestService;


    private static final String BASE_URL = "/v1/requests";

    @Test
    @DisplayName("요청 중 가장 많은 책 5개 정렬")
    @WithMockUser
    public void RequestRankTest() throws Exception {

        RequestDto.rankResponse response1 = new RequestDto.rankResponse(
                "book_title1", "author1", "publisher1","thumbnail1", 1L);
        RequestDto.rankResponse response2 = new RequestDto.rankResponse(
                "book_title2", "author2", "publisher2","thumbnail2",  2L);

        List<RequestDto.rankResponse> responseList = new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        given(requestService.findRankedRequests()).willReturn(responseList);

        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/rank")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("rank-request",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                        fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("저자"),
                                        fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                        fieldWithPath("data.[].thumbnail").type(JsonFieldType.STRING).description("요청 책 이미지"),
                                        fieldWithPath("data.[].count").type(JsonFieldType.NUMBER).description("같은 책 개수")


                                )
                        )
                ));


    }
}
