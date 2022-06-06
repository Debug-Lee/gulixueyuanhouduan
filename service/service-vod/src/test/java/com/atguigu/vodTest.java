package com.atguigu;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class vodTest {

    public static void main(String[] args) throws ClientException {

//        String accessKeyId="LTAI5t6tUyTHCXDVTrzmwFKb";
//        String accessKeySecret="OoTDnFSNYDMTAMK3OErBIGZ2KznP7Y";
//        String title="6 - What If I Want to Move Faster.mp4 by sdk";  //设置上传以后的文件名字
//        String fileName= "D:/StudyTools/尚硅谷项目资源/gulixiangmu/项目资料/1-阿里云上传测试视频/6 - What If I Want to Move Faster.mp4"; //设置上传文件的本地路径
//        //上传视频的request
//        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
//        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
//        request.setPartSize(2 * 1024 * 1024L);
//        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
//        request.setTaskNum(1);
//
//        //new一个uploader进行山川
//        UploadVideoImpl uploader = new UploadVideoImpl();
//        //获得返回的数据
//        UploadVideoResponse response = uploader.uploadVideo(request);
//        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
//        if (response.isSuccess()) {
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//        } else {
//            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//            System.out.print("ErrorCode=" + response.getCode() + "\n");
//            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
//        }
        getVideoAuth();

    }

    //根据视频ID获取视频凭证
    public static void  getVideoAuth() throws ClientException{
        //根据视频ID获取视频凭证
        //创建初始化对象
        DefaultAcsClient client= initVodClient.initVodClient("LTAI5t6tUyTHCXDVTrzmwFKb","OoTDnFSNYDMTAMK3OErBIGZ2KznP7Y");
        //创建获取视频对象的request 和 response
        GetVideoPlayAuthRequest request=new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response=new GetVideoPlayAuthResponse();

        //设置视频ID
        request.setVideoId("d2543c8a1ed6447bbf1e6706d5c6c224");
        //调用client中的方法，传递request
        response = client.getAcsResponse(request);

        //进行数据输出
        System.out.println("VideoAuth:"+response.getPlayAuth());
    }

    //根据视频ID获取视频URL
    public static void  getVideoUrl () throws ClientException
    {
        //根据视频ID获取视频播放地址
        //创建初始化对象
        DefaultAcsClient client= initVodClient.initVodClient("LTAI5t6tUyTHCXDVTrzmwFKb","OoTDnFSNYDMTAMK3OErBIGZ2KznP7Y");

        //创建获取视频对象的request 和 response对象
        GetPlayInfoRequest request=new GetPlayInfoRequest();
        GetPlayInfoResponse response=new GetPlayInfoResponse();

        //向request对象里面设置 视频ID
        request.setVideoId("01226122997448d394c4a1c515ac5de9");

        //调用client中的方法，传递request，获取数据
        response = client.getAcsResponse(request);

        //播放地址

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }
}
