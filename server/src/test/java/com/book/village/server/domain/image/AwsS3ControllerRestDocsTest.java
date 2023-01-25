package com.book.village.server.domain.image;

import com.book.village.server.global.utils.GenerateMockToken;
import com.book.village.server.image.AwsS3Controller;
import com.book.village.server.image.AwsS3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.book.village.server.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AwsS3Controller.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class AwsS3ControllerRestDocsTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AwsS3Service s3Service;

    private static final String url = "/v1/s3/images";

    @Test
    @DisplayName("이미지 업로드")
    @WithMockUser
    public void uploadImageTest() throws Exception{
        String imgUrl = "https://img.icons8.com/windows/32/null/user-male-circle.png";
        given(s3Service.uploadImg(Mockito.any(MultipartFile.class))).willReturn(imgUrl);
        ClassPathResource classPathResource = new ClassPathResource("testImage/logo.png");

        MockMultipartFile image1 = new MockMultipartFile(
                "image",
                "testImage/logo.png",
                "image/png",
                classPathResource.getInputStream()
        );

        ResultActions actions =
                mockMvc.perform(
                        multipart(url+"/upload")
                                .file(image1)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andDo(document("upload-image",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestParts(
                                partWithName("image").description("이미지 파일")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.STRING).description("이미지 url")
                        )
                ));
    }

    @Test
    @DisplayName("여러 이미지 업로드")
    @WithMockUser
    public void uploadImagesTest() throws Exception{
        String imgUrl = "https://img.icons8.com/windows/32/null/user-male-circle.png";
        List<String> imgUrlList = new ArrayList<>();
        imgUrlList.add(imgUrl);
        imgUrlList.add(imgUrl);
        given(s3Service.uploadImgs(Mockito.anyList())).willReturn(imgUrlList);
        ClassPathResource classPathResource = new ClassPathResource("testImage/logo.png");

        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                "testImage/logo.png",
                "images/png",
                classPathResource.getInputStream()
        );

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                "testImage/logo.png",
                "images/png",
                classPathResource.getInputStream()
        );

        ResultActions actions =
                mockMvc.perform(
                        multipart(url+"/upload/multi")
                                .file(image1).file(image2)
                                .with(csrf())
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andDo(document("upload-images",
                        getResponsePreProcessor(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestParameters(
                                parameterWithName("_csrf").description("csrf")
                        ),
                        requestParts(
                                partWithName("images").description("이미지 파일")
                        ),
                        responseFields(
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("이미지 url 리스트")
                        )
                ));
    }

    @Test
    @DisplayName("이미지 삭제")
    @WithMockUser
    public void deleteImageTest() throws Exception{
        doNothing().when(s3Service).deleteImage(Mockito.anyString());

        ResultActions actions =
                mockMvc.perform(
                        delete(url+"/delete?fileName=filename")
                                .accept(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(GenerateMockToken.getMockHeaderToken())
                );
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-image",
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer Token")
                        ),
                        requestParameters(
                                parameterWithName("fileName").description("파일 이름"),
                                parameterWithName("_csrf").description("csrf")
                        )
                ));
    }
}
