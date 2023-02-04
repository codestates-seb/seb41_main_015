package com.book.village.server.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.book.village.server.global.exception.CustomLogicException;
import com.book.village.server.global.exception.ExceptionCode;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadImg(MultipartFile multipartFile) throws IOException {
        String contentType = multipartFile.getContentType();
        if(ObjectUtils.isEmpty(contentType)) {
            throw new CustomLogicException(ExceptionCode.INVALID_FILE_CONTENT_TYPE);
        }
        else if(
                !(contentType.equals(ContentType.IMAGE_JPEG)) || !(contentType.equals(ContentType.IMAGE_PNG)) ||
                        !(contentType.equals(ContentType.IMAGE_BMP)) || !(contentType.equals(ContentType.IMAGE_GIF))
        ){
                    throw new CustomLogicException(ExceptionCode.CONTENT_TYPE_MISMATCH);
        }
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename(); // 이름 랜덤생성 + 파일이름

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getSize());
        objMeta.setContentType(multipartFile.getContentType()); // 파일 사이즈 전달

        try(InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, inputStream, objMeta)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); // s3 파일 업로드
        } catch(IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드를 실패했습니다."); // 업로드 실패
        }
        return amazonS3.getUrl(bucket, s3FileName).toString(); // 파일이름으로 버킷에서 url 가져오기
    }

    public List<String> uploadImgs(List<MultipartFile> multipartFile) {
        List<String> urlList = new ArrayList<>();
        for(MultipartFile mf: multipartFile){
            String contentType = mf.getContentType();
            if(ObjectUtils.isEmpty(contentType)) {
                throw new CustomLogicException(ExceptionCode.INVALID_FILE_CONTENT_TYPE);
            }
            else if(
                    !(contentType.equals(ContentType.IMAGE_JPEG)) || !(contentType.equals(ContentType.IMAGE_PNG)) ||
                            !(contentType.equals(ContentType.IMAGE_BMP)) || !(contentType.equals(ContentType.IMAGE_GIF)) ||
                            !(contentType.equals(ContentType.IMAGE_JPEG))
            ){
                throw new CustomLogicException(ExceptionCode.CONTENT_TYPE_MISMATCH);
            }
        }

        multipartFile.forEach(file -> {
            String s3FileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentLength(file.getSize());
            objMeta.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, inputStream, objMeta)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch(IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드를 실패했습니다.");
            }
            urlList.add(amazonS3.getUrl(bucket, s3FileName).toString());
        });

        return urlList;
    }

    public void deleteImage(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
}
