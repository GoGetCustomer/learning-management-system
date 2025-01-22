package com.example.lms.common.service;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final S3Operations s3Operations;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public void uploadToS3(MultipartFile multipartFile, String fileName) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Operations.upload(bucket, fileName, inputStream,
                    ObjectMetadata.builder()
                            .contentType(multipartFile.getContentType())
                            .build());
        }
        log.info("S3에 파일 업로드 완료: {} \n", fileName);
    }

    public String generateUniqueFileName(String originalFileName) {
        String sanitizedFileName = originalFileName.replace(" ", "_");
        String uuid = UUID.randomUUID().toString();
        return uuid + "_" + sanitizedFileName;
    }

    public String generateS3FileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    public void deleteFromS3(String url) {
        // URL에서 파일 이름 추출
        String fileName = url.substring(url.lastIndexOf("/") + 1);

        // S3에서 파일 삭제 요청
        s3Operations.deleteObject(bucket, fileName);

        log.info("S3에서 파일 삭제 완료: {} \n", fileName);
    }
}
