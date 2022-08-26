package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.product.config.minio.MinioProperties;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    MinioProperties minioProperties;
    @Autowired
    MinioClient minioClient;
    @Override
    public String upload(MultipartFile file) throws Exception {
        boolean b = minioClient.bucketExists(minioProperties.getBucketName());
        if(!b){
            minioClient.makeBucket(minioProperties.getBucketName());
        }
        String name=file.getName();
        String dateStr = DateUtil.formatDate(new Date());
        String filename= UUID.randomUUID().toString().replace("-","")+"_"
                +file.getOriginalFilename();

        InputStream inputStream = file.getInputStream();
        String contentType=file.getContentType();
        PutObjectOptions options = new PutObjectOptions(file.getSize(), -1L);
        options.setContentType(contentType);

        minioClient.putObject(
                minioProperties.getBucketName(),
                dateStr+"/"+filename,
                inputStream,options
        );
        String url = minioProperties.getEndpoint()+"/"+minioProperties.getBucketName()+"/"+dateStr+"/"+filename;
        return  url;
    }
}
