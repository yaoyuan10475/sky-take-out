package com.sky.controller.admin;

import com.aliyun.oss.AliOSSUtils;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    AliOSSUtils aliOSSUtils;

    @PostMapping("/upload")
    public Result<String> upload(@RequestBody MultipartFile file){
        try{
            String url = aliOSSUtils.upload(file);
            return Result.success(url);
        }catch (Exception e){
            log.error("文件上传失败", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
