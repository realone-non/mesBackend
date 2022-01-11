package com.mes.mesBackend.helper;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mes.mesBackend.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class S3Service implements S3Uploader {

    @Autowired
    AmazonS3 amazonS3;

    @Value("${cloud.aws.bucketName}")
    private String bucketName;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException, BadRequestException {
        if (multipartFile.isEmpty()) {
            throw new BadRequestException("file is empty");
        }

        if (multipartFile.getOriginalFilename() == null) {
            throw new BadRequestException("originalFileName is null");
        }

        String fileName = dirName + datePath();

        return putS3(multipartFile, fileName);
    }

    public String putS3(MultipartFile multipartFile, String fileName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, multipartFile.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(putObjectRequest);

        return amazonS3.getUrl(bucketName, fileName).toString();
    }


    public void delete(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    private String datePath() {
        String dateTimeFormat = "yyyyMMdd_HHmmss_SSS";
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }
}
