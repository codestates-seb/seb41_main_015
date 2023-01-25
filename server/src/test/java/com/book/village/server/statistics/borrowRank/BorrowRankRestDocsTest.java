package com.book.village.server.statistics.borrowRank;

import com.book.village.server.domain.borrow.controller.BorrowController;
import com.book.village.server.domain.borrow.dto.BorrowDto;
import com.book.village.server.domain.borrow.entity.BorrowRank;
import com.book.village.server.domain.borrow.mapper.BorrowMapper;
import com.book.village.server.domain.borrow.service.BorrowService;
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

@WebMvcTest(BorrowController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class BorrowRankRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowService borrowService;

    @MockBean
    private BorrowMapper mapper;

    private static final String BASE_URL = "/v1/borrows";

    @Test
    @DisplayName("요청 중 가장 많은 책 5개 정렬")
    @WithMockUser
    public void BorrowRankTest() throws Exception {

        BorrowDto.rankResponse response1 = new BorrowDto.rankResponse(
                "book_title1", "author1", "publisher1", 2L);
        BorrowDto.rankResponse response2 = new BorrowDto.rankResponse(
                "book_title2", "author2", "publisher2", 1L);

        List<BorrowDto.rankResponse> responseList = new ArrayList<>();
        responseList.add(response1);

        List<BorrowRank> BorrowRankList = new ArrayList<>();
        BorrowRank borrowRank = new BorrowRank() {
            @Override
            public String getBook_Title() {
                return null;
            }

            @Override
            public String getAuthor() {
                return null;
            }

            @Override
            public String getPublisher() {
                return null;
            }

            @Override
            public Long getCount() {
                return null;
            }
        };

        BorrowRankList.add(borrowRank);
        BorrowRankList.add(borrowRank);
        BorrowRankList.add(borrowRank);

        given(borrowService.findRankedBorrows()).willReturn(BorrowRankList);
        given(mapper.borrowRanksTorankedResponses(Mockito.anyList())).willReturn(responseList);

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
                .andDo(document("rank-borrow",
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
                                        fieldWithPath("data.[].count").type(JsonFieldType.NUMBER).description("같은 책 개수")

                                )
                        )
                ));

    }

}
