package com.atguigu.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.exceptionHandler.GuliException;
import com.atguigu.vod.Utils.ConstantPropertiesUtil;
import com.atguigu.vod.Utils.initVodClient;
import com.atguigu.vod.service.VideoService;
import com.atguigu.commonutils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {

    @Autowired
    VideoService videoService;

    //上传视频到阿里云
    @PostMapping("uploadAliVideo")
    public R uploadAliVideo(MultipartFile file)
    {
        String videoId=videoService.upLoadVideo(file);
        return R.ok().data("videoId",videoId);
    }

    //通过视频ID删除阿里云视频
    @DeleteMapping("removeAlyVideo/{videoId}")
    public R removeAlyVideo(@PathVariable String videoId)
    {
        try {
            //初始化对象
            DefaultAcsClient clent = initVodClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建delete的request
            DeleteVideoRequest request=new DeleteVideoRequest();
            //向request里面设置视频ID
            request.setVideoIds(videoId);
            //调用client中的删除方法
            clent.getAcsResponse(request);
            return R.ok();
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败！");
        }
    }

    //@RequestParam 绑定访问路径中参数 videoIdList   videoIdList=?
    //通过视频ID删除多个阿里云视频
    @DeleteMapping("deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList")List<String> videoIdList)
    {
        videoService.removeMoreAlyVideo(videoIdList);
        return R.ok();
    }

    //获取视频凭证
    @GetMapping("getPlayerAuth/{id}")
    public R getPlayerAuth(@PathVariable String id)
    {
        try {
        //初始化对象
        DefaultAcsClient clien = initVodClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        //创建request和response对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(id);

        GetVideoPlayAuthResponse response = clien.getAcsResponse(request);
        String playAuth = response.getPlayAuth();
        return R.ok().data("playAuth",playAuth);

        }catch (Exception e)
        {
            throw new GuliException(20001,"获取视频凭证失败！");
        }
    }
}
