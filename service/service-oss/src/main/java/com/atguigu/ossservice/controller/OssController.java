package com.atguigu.ossservice.controller;

import com.atguigu.ossservice.service.OssService;
import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description="上传文件管理")
@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    /**
     * 文件上传
     * @return  文件的url地址
     */
    @ApiOperation("上传文件接口")
    @PostMapping
    public R upload(MultipartFile file)
    {
        String url= ossService.uplodaFileAvatar(file);
        return R.ok().data("url",url);
    }
}
