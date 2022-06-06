package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.pojo.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 首页banner表 后台控制器
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-24
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService crmBannerService;

    //分页查询banner
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable Long page, @PathVariable Long limit)
    {
        Page<CrmBanner> bannerPage=new Page<>(page,limit);
        crmBannerService.page(bannerPage,null);

        return R.ok().data("items",bannerPage.getRecords()).data("total",bannerPage.getTotal());
    }

    //根据ID获取banner
    @GetMapping("getBanner/{id}")
    public R getBannerById(@PathVariable String id)
    {
        CrmBanner banner = crmBannerService.getById(id);
        return R.ok().data("item",banner);
    }

    //新增banner
    @PostMapping("save")
    public R save(@RequestBody CrmBanner banner)
    {
        crmBannerService.save(banner);
        return R.ok();
    }

    //更新banner
    @PostMapping("update")
    public R update(@RequestBody CrmBanner banner)
    {
        crmBannerService.updateById(banner);
        return R.ok();
    }

    //删除banner
    @DeleteMapping("delete/{id}")
    public R delete(@PathVariable String id)
    {
        crmBannerService.removeById(id);
        return R.ok();
    }

}

