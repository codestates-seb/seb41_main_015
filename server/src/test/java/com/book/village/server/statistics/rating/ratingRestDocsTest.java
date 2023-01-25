package com.book.village.server.statistics.rating;

import com.book.village.server.domain.book.controller.BookController;
import com.book.village.server.domain.book.dto.BookDto;
import com.book.village.server.domain.book.entity.Book;
import com.book.village.server.domain.book.mapper.BookMapper;
import com.book.village.server.domain.book.service.BookService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ratingRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper mapper;

    private static final String url = "/v1/books";

    @Test
    @DisplayName("평점 랭킹")
    @WithMockUser
    public void getRankingByRatingTest() throws Exception {

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;


        BookDto.Response response1=new BookDto.Response(
                1L,
                "isbn1",
                "bookTitle1",
                "author1",
                "publisher1",
                "thumbnail1",
                0.0,
                null,
                createdAt,
                createdAt
        );

        BookDto.Response response2=new BookDto.Response(
                2L,
                "isbn2",
                "bookTitle2",
                "author2",
                "publsher2",
                "thumbnail2",
                0.0,
                null,
                createdAt,
                createdAt
        );
        List<BookDto.Response> responseList=new ArrayList<>();
        responseList.add(response1);
        responseList.add(response2);

        List<Book> list=List.of(
                new Book(1L, "isbn1","bookTitle1","author1", "publisher1", "thumbnail1" ,0L,0L, 0.0, null),
                new Book(2L, "isbn2","bookTitle2","author2", "publisher2","thumbnail2" ,0L,0L, 0.0, null)
        );

        given(bookService.findBooks(Mockito.any(Pageable.class))).willReturn(new PageImpl<>(
                list,
                PageRequest.of(0,5,
                        Sort.by("avgRate").descending()),2
        ));
        given(mapper.booksToBookResponseDtos(Mockito.anyList())).willReturn(responseList);

        ResultActions actions =
                mockMvc.perform(
                        get(url)
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "avgRate,desc")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("statistics-books",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준[avgRate,desc]"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].bookId").type(JsonFieldType.NUMBER).description("도서 식별자"),
                                        fieldWithPath("data.[].isbn").type(JsonFieldType.STRING).description("isbn"),
                                        fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("도서 제목"),
                                        fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("저자"),
                                        fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("출판사"),
                                        fieldWithPath("data.[].thumbnail").type(JsonFieldType.STRING).description("도서 이미지"),
                                        fieldWithPath("data.[].avgRate").type(JsonFieldType.NUMBER).description("평점 평균"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("도서 생성 일자"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("도서 수정 일자"),
                                        fieldWithPath("data.[].rates").type(JsonFieldType.NULL).description("평점 정보"),
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
