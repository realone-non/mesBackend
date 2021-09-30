package com.mes.mesBackend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mes.mesBackend.helper.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class S3Service implements S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.image.bucket}")
    private String bucketName;

    @Override
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String localDateTime = LocalDateTime.now().toString();

        String fileName = dirName + "/" + localDateTime;

        return putS3(multipartFile, fileName);
    }
    @Override
    public String putS3(MultipartFile multipartFile, String fileName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, multipartFile.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(putObjectRequest);

        return amazonS3.getUrl(bucketName, fileName).toString(); // ==========================
    }

    @Override
    public void delete(String fileUrl) throws IOException {
        String fileName = "";
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }
}
