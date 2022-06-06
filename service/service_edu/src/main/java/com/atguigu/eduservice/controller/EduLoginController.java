package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@Api(description="登陆管理")
@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin  //解决跨域问题
public class EduLoginController {

    //token 登录
    @PostMapping("/login")
    public R login()
    {
        return R.ok().data("token","admin");
    }

    //info 获取用户信息
    @GetMapping("/info")
    public R info()
    {
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://images.weserv.nl/?url=https://tupian.qqw21.com/article/UploadPic/2019-6/20196271745942366.jpg?imageView2/1/w/80/h/80");
    }

}
