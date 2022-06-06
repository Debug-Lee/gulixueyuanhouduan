package com.atguigu.ossservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    //上传文件到OSS
    String uplodaFileAvatar(MultipartFile file);
}
