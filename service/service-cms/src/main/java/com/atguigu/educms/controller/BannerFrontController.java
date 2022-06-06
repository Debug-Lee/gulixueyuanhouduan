package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.pojo.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-24
 */
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
public class BannerFrontController {

    @Autowired
    private CrmBannerService crmBannerService;

    //获取首页所有的banner
    @GetMapping("getAllBanner")
    public R getAllBanner()
    {
        List<CrmBanner> list=crmBannerService.selectAllBanner();
        return R.ok().data("list",list);
    }

}

