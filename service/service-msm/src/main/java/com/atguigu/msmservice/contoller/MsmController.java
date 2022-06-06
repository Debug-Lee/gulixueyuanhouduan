package com.atguigu.msmservice.contoller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {


    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送短信的方法
    @GetMapping("send/{phone}")
    public R send(@PathVariable String phone)
    {
        //先查redis中是否有验证码，如果有，返回OK
        String code = redisTemplate.opsForValue().get("phone");
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }

        //通过工具类获得四位随机验证码
        code = RandomUtil.getFourBitRandom();
        //将验证码存入map中
        Map<String,Object> param= new HashMap<>();
        param.put("code",code);
        boolean isSend = msmService.send(param,phone);
        if(isSend)
        {
            //发送成功，将验证码存入redis中
            //设置验证码的有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("发送短信失败");
        }
    }
}
