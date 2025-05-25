package com.slt.slt.controller;

import com.slt.slt.Result.Result;
import com.slt.slt.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> uploadFile(MultipartFile file) throws Exception {
        // 把文件内容存到本地磁盘
        String originalFilename = file.getOriginalFilename();
        // 保证文件名字唯一 防止被覆盖
        String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        // file.transferTo(new File("D:\\MyDanlod\\files\\"+ filename));
        String url = AliOssUtil.uploadFile(filename,file.getInputStream());
        System.out.println("文件图片已经成功上传至"+ url);
        return Result.success(url);
    }
}
