package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.commonutils.R;
import com.atguigu.exceptionHandler.GuliException;
import com.atguigu.vod.Utils.ConstantPropertiesUtil;
import com.atguigu.vod.Utils.initVodClient;
import com.atguigu.vod.service.VideoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    //上传视频到阿里云
    @Override
    public String upLoadVideo(MultipartFile file) {

        try {
            //accessKeyId accessKeySecret
            String accessKeyId= ConstantPropertiesUtil.ACCESS_KEY_ID;
            String accessKeySecret=ConstantPropertiesUtil.ACCESS_KEY_SECRET;
            //fileName  上传之前的文件名字
            String fileName=file.getOriginalFilename();
            //title 上传以后的文件名字  设置为去掉最后一个.以外的名字
            String title=fileName.substring(0, fileName.lastIndexOf("."));
            //inputSteam 视频输入流
            InputStream inputStream=file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                return response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                return response.getVideoId();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }



    }


    //通过视频ID删除多个阿里云视频
    @Override
    public void removeMoreAlyVideo(List videoIdList) {

        try {
            //初始化对象
            DefaultAcsClient clent = initVodClient.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //创建delete的request
            DeleteVideoRequest request=new DeleteVideoRequest();
            //将视频ID集合，转换成 1，2，3的string格式
            //StringUtils.join   前面参数为：需要转换的数组   separator ：表示用什么符号分割
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");
            //向request里面设置视频ID
            request.setVideoIds(videoIds);
            //调用client中的删除方法
            clent.getAcsResponse(request);
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败！");
        }
    }

}
