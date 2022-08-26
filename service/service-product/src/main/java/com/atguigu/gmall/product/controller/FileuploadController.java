package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@Api(tags = "文件上传控制器")
@RestController
@RequestMapping("/admin/product")
public class FileuploadController {
    @Autowired
    FileUploadService fileUploadService;

    @ApiOperation(value = "文件上传")
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file")MultipartFile file)throws  Exception{
        String url = fileUploadService.upload(file);
        return Result.ok(url);
    }
}
