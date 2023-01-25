package com.book.village.server.domain.book;

import com.book.village.server.domain.book.controller.BookController;
import com.book.village.server.domain.book.dto.BookDto;
import com.book.village.server.domain.book.entity.Book;
import com.book.village.server.domain.book.mapper.BookMapper;
import com.book.village.server.domain.book.service.BookService;
import com.book.village.server.domain.rate.dto.RateDto;
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

@WebMvcTest(BookController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class BookRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper mapper;

    @Autowired
    private Gson gson;

    private static final String url = "/v1/books";

    @Test
    @DisplayName("도서 수정")
    @WithMockUser
    public void patchBookTest() throws Exception {
        long bookId=1L;

        BookDto.Patch patch = new BookDto.Patch(
                bookId,
                "isbn1",
                "bookTitle1",
                "author1",
                "publisher1",
                "thumbnail",
                1L,
                1L,
                1.0
        );
        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;
        String content = gson.toJson(patch);

        List<RateDto.Response> rateResponse = List.of(
                new RateDto.Response(1L, 3L, "displayName1","img1", "title1", "content1", createdAt, createdAt),
                new RateDto.Response(2L, 3L, "displayName2","img2", "title2", "content2", createdAt, createdAt)
        );
        BookDto.Response response=new BookDto.Response(
                bookId,
                "isbn1",
                "bookTitle1",
                "author1",
                "publisher1",
                "thumbnail1",
                0.0,
                rateResponse,
                createdAt,
                createdAt
        );

        given(mapper.bookPatchDtoToBook(Mockito.any(BookDto.Patch.class))).willReturn(new Book());
        given(bookService.updateBook(Mockito.any(Book.class),Mockito.anyLong())).willReturn(new Book());
        given(mapper.bookToBookResponseDto(Mockito.any(Book.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        patch(url+"/{book-id}",bookId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isbn").value(patch.getIsbn()))
                .andExpect(jsonPath("$.data.bookTitle").value(patch.getBookTitle()))
                .andExpect(jsonPath("$.data.author").value(patch.getAuthor()))
                .andExpect(jsonPath("$.data.publisher").value(patch.getPublisher()))
                .andDo(document("patch-book",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("book-id").description("도서 식별자")
                        ),
                        // request body
                        requestFields(
                                List.of(
                                        fieldWithPath("bookId").type(JsonFieldType.NUMBER).description("도서 식별자").ignored(),
                                        fieldWithPath("isbn").type(JsonFieldType.STRING).description("isbn").optional(),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("도서 제목").optional(),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("저자").optional(),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("출판사").optional(),
                                        fieldWithPath("thumbnail").type(JsonFieldType.STRING).description("도서 이미지").optional(),
                                        fieldWithPath("totalRate").type(JsonFieldType.NUMBER).description("저자").ignored(),
                                        fieldWithPath("rateCount").type(JsonFieldType.NUMBER).description("출판사").ignored(),
                                        fieldWithPath("avgRate").type(JsonFieldType.NUMBER).description("도서 이미지").ignored()
                                )
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.bookId").type(JsonFieldType.NUMBER).description("도서 식별자"),
                                        fieldWithPath("data.isbn").type(JsonFieldType.STRING).description("isbn"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("도서 제목"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("저자"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("출판사"),
                                        fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("도서 이미지"),
                                        fieldWithPath("data.avgRate").type(JsonFieldType.NUMBER).description("평점 평균"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("도서 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("도서 수정 일자"),
                                        fieldWithPath("data.rates").type(JsonFieldType.ARRAY).description("평점 정보"),
                                        fieldWithPath("data.rates.[].rateId").type(JsonFieldType.NUMBER).description("평점 식별자"),
                                        fieldWithPath("data.rates.[].rating").type(JsonFieldType.NUMBER).description("평점"),
                                        fieldWithPath("data.rates.[].displayName").type(JsonFieldType.STRING).description("평점 작성자"),
                                        fieldWithPath("data.rates.[].imgUrl").type(JsonFieldType.STRING).description("평점 작성자 프로필 이미지"),
                                        fieldWithPath("data.rates.[].title").type(JsonFieldType.STRING).description("평점 제목"),
                                        fieldWithPath("data.rates.[].content").type(JsonFieldType.STRING).description("평점 내용"),
                                        fieldWithPath("data.rates.[].createdAt").type(JsonFieldType.STRING).description("평점 생성 일자"),
                                        fieldWithPath("data.rates.[].modifiedAt").type(JsonFieldType.STRING).description("평점 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("도서 조회")
    @WithMockUser
    public void getBookTest() throws Exception {
        long bookId=1L;

        LocalDateTime createdAt=LocalDateTime.now();
        LocalDateTime modifiedAt=createdAt;

        List<RateDto.Response> rateResponse = List.of(
                new RateDto.Response(1L, 3L, "displayName1", "imgUrl1", "title1", "content1", createdAt, createdAt),
                new RateDto.Response(2L, 3L, "displayName2","imgUrl2", "title2", "content2", createdAt, createdAt)
        );
        BookDto.Response response=new BookDto.Response(
                bookId,
                "isbn1",
                "bookTitle1",
                "author1",
                "publisher1",
                "thumbnail1",
                0.0,
                rateResponse,
                createdAt,
                createdAt
        );

        given(bookService.findBook(Mockito.anyLong())).willReturn(new Book());
        given(mapper.bookToBookResponseDto(Mockito.any(Book.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        get(url+"/{book-id}",bookId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("get-book",
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("book-id").description("도서 식별자")
                        ),
                        // response body
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.bookId").type(JsonFieldType.NUMBER).description("도서 식별자"),
                                        fieldWithPath("data.isbn").type(JsonFieldType.STRING).description("isbn"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("도서 제목"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("저자"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("출판사"),
                                        fieldWithPath("data.thumbnail").type(JsonFieldType.STRING).description("도서 이미지"),
                                        fieldWithPath("data.avgRate").type(JsonFieldType.NUMBER).description("평점 평균"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("도서 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("도서 수정 일자"),
                                        fieldWithPath("data.rates").type(JsonFieldType.ARRAY).description("평점 정보"),
                                        fieldWithPath("data.rates.[].rateId").type(JsonFieldType.NUMBER).description("평점 식별자"),
                                        fieldWithPath("data.rates.[].rating").type(JsonFieldType.NUMBER).description("평점"),
                                        fieldWithPath("data.rates.[].displayName").type(JsonFieldType.STRING).description("평점 작성자"),
                                        fieldWithPath("data.rates.[].imgUrl").type(JsonFieldType.STRING).description("평점 작성자 프로필 이미지"),
                                        fieldWithPath("data.rates.[].title").type(JsonFieldType.STRING).description("평점 제목"),
                                        fieldWithPath("data.rates.[].content").type(JsonFieldType.STRING).description("평점 내용"),
                                        fieldWithPath("data.rates.[].createdAt").type(JsonFieldType.STRING).description("평점 생성 일자"),
                                        fieldWithPath("data.rates.[].modifiedAt").type(JsonFieldType.STRING).description("평점 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("모든 도서 조회")
    @WithMockUser
    public void getBooksTest() throws Exception {

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
                        Sort.by("createdAt").descending()),2
        ));
        given(mapper.booksToBookResponseDtos(Mockito.anyList())).willReturn(responseList);

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
                .andDo(document("get-books",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준[createdAt,desc]"),
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

    @Test
    @DisplayName("도서 검색")
    @WithMockUser
    public void searchBooksTest() throws Exception {

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
                "publisher2",
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
                new Book(1L, "isbn1","bookTitle1","author1", "publisher1","thumbnail1",0L,0L, 0.0, null),
                new Book(2L, "isbn2","bookTitle2","author2", "publisher2","thumbnail2",0L,0L, 0.0, null)
        );

        given(bookService.searchBooks(Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(
                list,
                PageRequest.of(0,5,
                        Sort.by("createdAt").descending()),2
        ));
        given(mapper.booksToBookResponseDtos(Mockito.anyList())).willReturn(responseList);

        ResultActions actions =
                mockMvc.perform(
                        get(url+"/search?field=isbn&keyword=this")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "createdAt,desc")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("search-books",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("keyword").description("검색어"),
                                parameterWithName("field").description("검색 대상[isbn,bookTitle, author, publisher]"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준[createdAt,desc]"),
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
    @Test
    @DisplayName("도서 삭제")
    @WithMockUser
    public void deleteBookTest() throws Exception {
        long bookId=1L;
        doNothing().when(bookService).deleteBook(Mockito.anyLong());

        ResultActions actions =
                mockMvc.perform(
                        delete(url+"/{book-id}",bookId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-book",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("book-id").description("도서 식별자")
                        )
                ));
    }
}
