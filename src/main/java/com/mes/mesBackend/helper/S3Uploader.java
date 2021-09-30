package com.mes.mesBackend.helper;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Uploader {

    String upload(MultipartFile multipartFile, String dirName) throws IOException;

    String putS3(MultipartFile multipartFile, String fileName) throws IOException;

    void delete(String fileUrl) throws IOException;
}
