package com.atguigu.ossservice.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.atguigu.ossservice.service.OssService;
import com.atguigu.ossservice.utils.ConstantPropertiesUtil;
import io.swagger.annotations.Api;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Service
public class OssServiceImpl implements OssService {

    @Override
    public String uplodaFileAvatar(MultipartFile file) {
        //工具类获取值
        String endpoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
        try {
        //获取上传文件的原名
        String objectName = file.getOriginalFilename();

        //在文件名称里面添加随机唯一的值
        String uuid = UUID.randomUUID().toString().replace("-","");
        //yuaskdj2131234994.jpg
        objectName=uuid+objectName;

        //根据日期进行分类
        //使用依赖中的工具类获取日期
        String datePath= DateTime.now().toString("yyyy/MM/dd/");
        objectName=datePath+objectName;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //获取上传文件输入流
        InputStream inputStream = file.getInputStream();
        // 创建PutObject请求。
            //第一个参数 ：bucketName
            //第二个参数：上传到oss的文件路径和文件名称   /aa/bb/1.jpg
            //第三个参数：上传文件输入流
            ossClient.putObject(bucketName, objectName, inputStream);
            //关闭OSSClient
            ossClient.shutdown();
            //把上传之后文件路径返回
            //需要把上传到阿里云OSS路径手动拼起来
            //https://edu-guli-bulefat.oss-cn-beijing.aliyuncs.com/%E7%85%A7%E7%89%87.jpg
            String url="https://"+bucketName+"."+endpoint+"/"+objectName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
