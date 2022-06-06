package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {


    //发送短信的方法
    @Override
    public boolean send(Map<String, Object> param, String phone) {

        if(StringUtils.isEmpty(phone)) return false;

        //初始化
        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI5t6tUyTHCXDVTrzmwFKb", "OoTDnFSNYDMTAMK3OErBIGZ2KznP7Y");
        IAcsClient client = new DefaultAcsClient(profile);

        //设置相关参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置手机号，短信签名，模板code，验证码
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "阿里云短信测试");
        request.putQueryParameter("TemplateCode", "SMS_154950909");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        //调用方法，进行上传阿里云，阿里云接受到进行短信服务
        try {
            CommonResponse response=client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();//获取response查看是否上传成功
            return success;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
