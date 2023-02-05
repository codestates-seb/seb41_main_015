package com.book.village.server.domain.refreshToken;

import com.book.village.server.auth.jwt.tokenizer.JwtTokenizer;
import com.book.village.server.auth.jwt.controller.RefreshTokenController;
import com.book.village.server.auth.jwt.service.RefreshTokenService;
import com.book.village.server.global.utils.GenerateMockToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(RefreshTokenController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class RefreshTokenControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenizer jwtTokenizer;

    @MockBean
    private RefreshTokenService tokenService;

    private static final String url = "/auth/token";

    public RefreshTokenControllerRestDocsTest() throws Exception {
    }

    @Test
    @DisplayName("액세스 토큰 재발급")
    @WithMockUser
    public void getAccessTokenTest() throws Exception {
        given(jwtTokenizer.encodeBase64SecretKey(Mockito.anyString())).willReturn("");
        given(jwtTokenizer.getEmailFromToken(Mockito.anyString(), Mockito.anyString())).willReturn("");
        doNothing().when(tokenService).verifyToken(Mockito.anyString(),Mockito.anyString());
        given(tokenService.getAccessToken(Mockito.anyString())).willReturn("");

        ResultActions actions =
                mockMvc.perform(
                        post(url)
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderRefreshToken())
                );
        actions
                .andExpect(status().isOk())
                .andDo(document("getAccessToken-refreshToken",
                        requestHeaders(
                                headerWithName("Authorization").description("refresh token")
                        ),
                        responseHeaders(
                                headerWithName("Authorization").description("new created access token")
                        )
                ));

    }
}
