package com.book.village.server.image;

import com.book.village.server.global.response.ListResponse;
import com.book.village.server.global.response.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/s3/images")
public class AwsS3Controller {

    private final AwsS3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity uploadImage(@RequestParam MultipartFile image, Principal principal) throws IOException {
        return new ResponseEntity<>(new SingleResponse<>(s3Service.uploadImg(image)), HttpStatus.CREATED);
    }
    @PostMapping("/upload/multi")
    public ResponseEntity uploadImages(@RequestParam List<MultipartFile> images, Principal principal) throws IOException {
        return new ResponseEntity<>(new ListResponse<>(s3Service.uploadImgs(images)), HttpStatus.CREATED);
    }
    @DeleteMapping("/delete")
    public ResponseEntity deleteImage(@RequestParam String fileName, Principal principal) throws IOException {
        s3Service.deleteImage(fileName);
        return ResponseEntity.noContent().build();
    }

}
