package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    //上传视频到阿里云
    String upLoadVideo(MultipartFile file);

    //通过视频ID删除多个阿里云视频
    void removeMoreAlyVideo(List videoIdList);
}
