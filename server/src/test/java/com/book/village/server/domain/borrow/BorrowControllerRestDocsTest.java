package com.book.village.server.domain.borrow;

import com.book.village.server.auth.jwt.repository.RefreshTokenRepository;
import com.book.village.server.domain.borrow.controller.BorrowController;
import com.book.village.server.domain.borrow.dto.BorrowDto;
import com.book.village.server.domain.borrow.entity.Borrow;
import com.book.village.server.domain.borrow.mapper.BorrowMapper;
import com.book.village.server.domain.borrow.service.BorrowService;
import com.book.village.server.domain.borrowcomment.dto.BorrowCommentDto;
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

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BorrowController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class BorrowControllerRestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowMapper borrowMapper;

    @MockBean
    private BorrowService borrowService;

    @Autowired
    private Gson gson;

    private static final String BASE_URL = "/v1/borrows";

    private static final LocalDateTime time = LocalDateTime.now();


    @Test
    @DisplayName("나눔 게시글 작성")
    @WithMockUser
    void postBorrowTest() throws Exception {
        // given
        LocalDateTime createdAt = time;
        LocalDateTime modifiedAt = LocalDateTime.now();
        BorrowDto.Post post =
                new BorrowDto.Post("title",
                        "content",
                        "bookTitle",
                        "author",
                        "publisher",
                        "displayName",
                        "talkUrl");

        String content = gson.toJson(post);

        List<BorrowCommentDto.Response> borrowCommentResponse = List.of(
                new BorrowCommentDto.Response(
                1L,"content1","displayName1",createdAt, modifiedAt),
                new BorrowCommentDto.Response(
                2L, "content2", "displayName2", createdAt, modifiedAt)
        );

        BorrowDto.Response responseDto = BorrowDto.Response.builder().borrowId(1L)
                .title("title")
                .content("content")
                .bookTitle("bookTitle")
                .author("author")
                .publisher("publisher")
                .displayName("displayName")
                .talkUrl("talkUrl")
                .borrowComments(borrowCommentResponse)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();


        given(borrowMapper.borrowDtoPostToBorrow(Mockito.any(BorrowDto.Post.class))).willReturn(new Borrow());
        given(borrowService.createBorrow(Mockito.any(Borrow.class), Mockito.anyString())).willReturn(new Borrow());
        given(borrowMapper.borrowToBorrowDtoResponse(Mockito.any(Borrow.class))).willReturn(responseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post(BASE_URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );


        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(post.getTitle()))
                .andExpect(jsonPath("$.data.content").value(post.getContent()))
                .andExpect(jsonPath("$.data.bookTitle").value(post.getBookTitle()))
                .andExpect(jsonPath("$.data.author").value(post.getAuthor()))
                .andExpect(jsonPath("$.data.publisher").value(post.getPublisher()))
                .andExpect(jsonPath("$.data.displayName").value(post.getDisplayName()))
                .andExpect(jsonPath("$.data.talkUrl").value(post.getTalkUrl()))
                .andDo(document("post-borrow",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("나눔게시글 본문"),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description(" 제목"),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("displayName").type(JsonFieldType.STRING).description("나눔 게시글 작성자 닉네임"),
                                        fieldWithPath("talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.borrowId").type(JsonFieldType.NUMBER).description("나눔 게시글 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("나눌 책 제목"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("나눔 게시글 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("나눔 게시글 최신 수정 일자"),
                                        fieldWithPath("data.borrowComments").type(JsonFieldType.ARRAY).description("댓글 정보"),
                                        fieldWithPath("data.borrowComments.[].borrowCommentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("data.borrowComments.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("data.borrowComments.[].displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                        fieldWithPath("data.borrowComments.[].createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.borrowComments.[].modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                )
                        )

                ));
    }


    @Test
    @DisplayName("나눔 게시글 수정😊😊😊😊")
    @WithMockUser
    void patchBorrowTest() throws Exception {
        // given
        Long borrowId = 1L;
        LocalDateTime createdAt = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        BorrowDto.Patch patch =
                new BorrowDto.Patch(borrowId,
                        "title",
                        "content",
                        "bookTitle",
                        "author",
                        "publisher",
                        "talkUrl");
        String content = gson.toJson(patch);

        List<BorrowCommentDto.Response> borrowCommentResponse = List.of(
                new BorrowCommentDto.Response(
                        1L, "content1", "displayName1", createdAt, modifiedAt)
                ,new BorrowCommentDto.Response(
                         2L, "content2", "displayName2", createdAt, modifiedAt)
                );

        BorrowDto.Response responseDto = BorrowDto.Response.builder().borrowId(1L)
                .title("title")
                .content("content")
                .bookTitle("bookTitle")
                .author("author")
                .publisher("publisher")
                .displayName("displayName")
                .talkUrl("talkUrl")
                .borrowComments(borrowCommentResponse)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();


        given(borrowMapper.borrowDtoPatchToBorrow(Mockito.any(BorrowDto.Patch.class))).willReturn(new Borrow());

        given(borrowService.updateBorrow(Mockito.any(Borrow.class), Mockito.anyString())).willReturn(new Borrow());

        given(borrowMapper.borrowToBorrowDtoResponse(Mockito.any(Borrow.class))).willReturn(responseDto);

        // when
        ResultActions actions =
                mockMvc.perform(
                        patch(BASE_URL + "/{borrow-id}", borrowId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                                .headers(GenerateMockToken.getMockHeaderToken()));


        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.borrowId").value(patch.getBorrowId()))
                .andExpect(jsonPath("$.data.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.data.content").value(patch.getContent()))
                .andExpect(jsonPath("$.data.bookTitle").value(patch.getBookTitle()))
                .andExpect(jsonPath("$.data.author").value(patch.getAuthor()))
                .andExpect(jsonPath("$.data.publisher").value(patch.getPublisher()))
                .andExpect(jsonPath("$.data.talkUrl").value(patch.getTalkUrl()))
                .andDo(document("patch-borrow",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("borrow-id").description("나눔 식별자")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("borrowId").type(JsonFieldType.NUMBER).description("나눔 식별자").ignored(),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("나눔게시글 제목").optional(),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("나눔게시글 본문").optional(),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description(" 제목").optional(),
                                        fieldWithPath("author").type(JsonFieldType.STRING).description("나눌 책 저자").optional(),
                                        fieldWithPath("publisher").type(JsonFieldType.STRING).description("나눌 책 출판사").optional(),
                                        fieldWithPath("talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크").optional()
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.borrowId").type(JsonFieldType.NUMBER).description("나눔 게시글 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("나눌 책 제목"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("나눔 게시글 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("나눔 게시글 최신 수정 일자"),
                                        fieldWithPath("data.borrowComments").type(JsonFieldType.ARRAY).description("댓글 정보"),
                                        fieldWithPath("data.borrowComments.[].borrowCommentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("data.borrowComments.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("data.borrowComments.[].displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                        fieldWithPath("data.borrowComments.[].createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.borrowComments.[].modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                )
                        )

                ));
    }


    @Test
    @DisplayName("나눔 게시글 하나 조회😁😁")
    @WithMockUser
    void getBorrowTest() throws Exception {
        // given
        Long borrowId = 1L;
        LocalDateTime createdAt = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        // BorrowCommentDto.Respons들을 담는 list
        List<BorrowCommentDto.Response> list = new ArrayList<>();

        BorrowCommentDto.Response commentResponse1 =
                new BorrowCommentDto.Response(
                        1L,
                        "borrowContent1",
                        "displayName1",
                        createdAt,
                        modifiedAt
                );

        BorrowCommentDto.Response commentResponse2 =
                new BorrowCommentDto.Response(
                        2L,
                        "borrowContent2",
                        "displayName2",
                        createdAt,
                        modifiedAt
                );

        BorrowCommentDto.Response commentResponse3 =
                new BorrowCommentDto.Response(
                        3L,
                        "borrowContent3",
                        "displayName3",
                        createdAt,
                        modifiedAt
                );

        list.add(commentResponse1);
        list.add(commentResponse2);
        list.add(commentResponse3);


        BorrowDto.Response responseDto = BorrowDto.Response.builder().borrowId(1L)
                .title("title")
                .content("content")
                .bookTitle("bookTitle")
                .author("author")
                .publisher("publisher")
                .displayName("displayName")
                .talkUrl("talkUrl")
                .borrowComments(list)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();

        given(borrowService.findVerificationBorrow(Mockito.any(Long.class))).willReturn(new Borrow());
        given(borrowMapper.borrowToBorrowDtoResponse(Mockito.any(Borrow.class))).willReturn(responseDto);



        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL + "/{borrow-id}", borrowId)
                                .with(csrf())
                        // JSON으로 받을 내용이 없음.
                );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.borrowId").value(responseDto.getBorrowId()))
                .andExpect(jsonPath("$.data.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.data.content").value(responseDto.getContent()))
                .andExpect(jsonPath("$.data.bookTitle").value(responseDto.getBookTitle()))
                .andExpect(jsonPath("$.data.author").value(responseDto.getAuthor()))
                .andExpect(jsonPath("$.data.publisher").value(responseDto.getPublisher()))
                .andExpect(jsonPath("$.data.talkUrl").value(responseDto.getTalkUrl()))
                .andDo(document("get-borrow",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("borrow-id").description("나눔 식별자")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                                        fieldWithPath("data.borrowId").type(JsonFieldType.NUMBER).description("나눔 게시글 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.bookTitle").type(JsonFieldType.STRING).description("나눌 책 제목"),
                                        fieldWithPath("data.author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("data.publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("data.displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.borrowComments").type(JsonFieldType.ARRAY).description("나눔 댓글 리스트"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("나눔 게시글 생성 일자"),
                                        fieldWithPath("data.modifiedAt").type(JsonFieldType.STRING).description("나눔 게시글 최신 수정 일자"),
                                        fieldWithPath("data.borrowComments.[].borrowCommentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("data.borrowComments.[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("data.borrowComments.[].displayName").type(JsonFieldType.STRING).description("댓글 작성자"),
                                        fieldWithPath("data.borrowComments.[].createdAt").type(JsonFieldType.STRING).description("댓글 생성 일자"),
                                        fieldWithPath("data.borrowComments.[].modifiedAt").type(JsonFieldType.STRING).description("댓글 수정 일자")
                                )
                        )
                ));
    }

    @Test
    @DisplayName("나눔 전체 조회😁😁😁")
    @WithMockUser
    void getBorrowsTest() throws Exception {
        // given // 나눔 전체는 댓글 정보 필요 X
        LocalDateTime createdAt = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        // Response를 담을 List
        List<BorrowDto.Response> borrowResponseDtos = new ArrayList<>();

        BorrowDto.Response borrowResponse1 = BorrowDto.Response.builder()
                .borrowId(1L)
                .title("title1")
                .content("content1")
                .bookTitle("bookTitle1")
                .author("author1")
                .publisher("publisher1")
                .displayName("displayName1")
                .talkUrl("talkUrl1")
                .borrowComments(null)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();

        BorrowDto.Response borrowResponse2 = BorrowDto.Response.builder()
                .borrowId(2L)
                .title("title2")
                .content("content2")
                .bookTitle("bookTitle2")
                .author("author2")
                .publisher("publisher2")
                .displayName("displayName2")
                .talkUrl("talkUrl2")
                .borrowComments(null)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();

        borrowResponseDtos.add(borrowResponse1);
        borrowResponseDtos.add(borrowResponse2);

        List<Borrow> borrows = new ArrayList<>();
        borrows.add(new Borrow(1L,
                "title1",
                "content1",
                "bookTitle1",
                "author1",
                "publisher1",
                "displayName1",
                "talkUrl1",
                new Member(),
                null));

        borrows.add(new Borrow(2L,
                "title2",
                "content2",
                "bookTitle2",
                "author2",
                "publisher2",
                "displayName2",
                "talkUrl2",
                new Member(),
                null));


        given(borrowService.findBorrows(Mockito.any(Pageable.class))).willReturn((new PageImpl<>(
                borrows, PageRequest.of(0, 10,
                Sort.by("createdAt").descending()), 2)));
        given(borrowMapper.borrowsToBorrowResponseDtos(Mockito.anyList())).willReturn(borrowResponseDtos);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL)
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "borrowId,desc")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                );

        // than
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("get-borrows",
                        getRequestPreProcessor(),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준[createdAt,desc"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].borrowId").type(JsonFieldType.NUMBER).description("나눔 게시글 식별자"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("나눌 책 제목"),
                                        fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.[].talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("나눔 게시글 생성 일자"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("나눔 게시글 최신 수정 일자"),
                                        fieldWithPath("data.[].borrowComments").type(JsonFieldType.NULL).description("댓글 정보"),
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
    @DisplayName("나의 모든 나눔글 조회")
    @WithMockUser
    public void getMyBorrows() throws Exception {
        // given
        LocalDateTime createdAt = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        List<BorrowDto.Response> borrowsResponseDtos = new ArrayList<>();

        BorrowDto.Response response1 = new BorrowDto.Response(
                1L,
                "title1",
                "content1",
                "bookTitle1",
                "author1",
                "publisher1",
                "displayName1",
                "talkUrl1",
                null,
                createdAt,
                modifiedAt
        );
        BorrowDto.Response response2 = new BorrowDto.Response(
                2L,
                "title2",
                "content2",
                "bookTitle2",
                "author2",
                "publisher2",
                "displayName2",
                "talkUrl2",
                null,
                createdAt,
                modifiedAt
        );

        borrowsResponseDtos.add(response1);
        borrowsResponseDtos.add(response2);

        List<Borrow> borrows = new ArrayList<>();
        borrows.add(new Borrow(1L,
                "title1",
                "content1",
                "bookTitle1",
                "author1",
                "publisher1",
                "displayName1",
                "talkUrl1",
                new Member(),
                null
                ));

        borrows.add(new Borrow(2L,
                "title2",
                "content2",
                "bookTitle2",
                "author2",
                "publisher2",
                "displayName2",
                "talkUrl2",
                new Member(),
                null
        ));

        given(borrowService.findMyBorrows(Mockito.anyString(), Mockito.any(Pageable.class))).willReturn(new PageImpl<>(
                borrows,PageRequest.of(0,10,Sort.by("createdAt").descending()),2));
        given(borrowMapper.borrowsToBorrowResponseDtos(Mockito.anyList())).willReturn(borrowsResponseDtos);

        // when
        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL +"/mine")
                                .param("page","0")
                                .param("size","10")
                                .param("sort","borrowId,desc")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andDo(document("get-borrows-mine",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준[createdAt,desc]"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].borrowId").type(JsonFieldType.NUMBER).description("나눔 게시글 식별자"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("나눌 책 제목"),
                                        fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.[].talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("나눔 게시글 생성 일자"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("나눔 게시글 최신 수정 일자"),
                                        fieldWithPath("data.[].borrowComments").type(JsonFieldType.NULL).description("댓글 정보"),
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
    @DisplayName("나눔 삭제")
    @WithMockUser
    void deleteBorrowTest() throws Exception {
        Long borrowId = 1L;

        LocalDateTime createdAt = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        List<BorrowCommentDto.Response> borrowCommentResponse =
                List.of(
                        new BorrowCommentDto.Response(
                         1L,
                                 "content1",
                             "displayName1",
                                        createdAt,
                                        modifiedAt),
                        new BorrowCommentDto.Response(
                                2L,
                                "content2",
                                "displayName2",
                                createdAt,
                                modifiedAt)
                );

        doNothing().when(borrowService).deleteBorrow(Mockito.anyLong(), Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        delete(BASE_URL+"/{borrow-id}", borrowId)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-borrow",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("borrow-id").description("나눔 글 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("나눔 검색")
    @WithMockUser
    void searchBorrowTest() throws Exception {

        LocalDateTime createdAt = time;
        LocalDateTime modifiedAt = LocalDateTime.now();

        BorrowDto.Response response1 = new BorrowDto.Response(
                1L,
                "title1",
                "content1",
                "bookTitle1",
                "author1",
                "publisher1",
                "displayName1",
                "talkUrl1",
                null,
                createdAt,
                modifiedAt
        );
        BorrowDto.Response response2 = new BorrowDto.Response(
                2L,
                "title2",
                "content2",
                "bookTitle2",
                "author2",
                "publisher2",
                "diisplayName2",
                "talkUrl2",
                null,
                createdAt,
                modifiedAt
        );

        List<BorrowDto.Response> borrowResponseDtos = new ArrayList<>();
        borrowResponseDtos.add(response1);
        borrowResponseDtos.add(response2);

        List<Borrow> borrows = new ArrayList<>();
        borrows.add(new Borrow(
                1L,
                "title1",
                "content1",
                "bookTitle1",
                "author1",
                "publisher1",
                "displayName1",
                "talkUrl1",
                new Member(),
                null)
        );
        borrows.add(new Borrow(
                2L,
                "title2",
                "content2",
                "bookTitle2",
                "author2",
                "publisher2",
                "displayName2",
                "talkUrl",
                new Member(),
                null)
        );

        given(borrowService.searchBorrow(Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(
                        borrows, PageRequest.of(0, 5, Sort.by("createdAt").descending()),2));
        given(borrowMapper.borrowsToBorrowResponseDtos(Mockito.anyList())).willReturn(borrowResponseDtos);


        ResultActions actions =
                mockMvc.perform(
                        get(BASE_URL+"/search?field=title&keyword=this")
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
                .andDo(document("search-borrows",
                        getResponsePreProcessor(),
                        requestParameters(
                                parameterWithName("keyword").description("검색어"),
                                parameterWithName("field").description("검색 대상[title, content, displayName]"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("sort").description("정렬 기준[createdAt,desc]"),
                                parameterWithName("_csrf").description("csrf")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터"),
                                        fieldWithPath("data.[].borrowId").type(JsonFieldType.NUMBER).description("나눔 식별자"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("나눔게시글 제목"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("게시글 본문"),
                                        fieldWithPath("data.[].bookTitle").type(JsonFieldType.STRING).description("나눌 책 제목"),
                                        fieldWithPath("data.[].author").type(JsonFieldType.STRING).description("나눌 책 저자"),
                                        fieldWithPath("data.[].publisher").type(JsonFieldType.STRING).description("나눌 책 출판사"),
                                        fieldWithPath("data.[].displayName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.[].talkUrl").type(JsonFieldType.STRING).description("오픈톡 링크"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("생성 일자"),
                                        fieldWithPath("data.[].modifiedAt").type(JsonFieldType.STRING).description("수정 일자"),
                                        fieldWithPath("data.[].borrowComments").type(JsonFieldType.NULL).description("댓글 정보"),
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