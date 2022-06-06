package com.atguigu.educms.service;

import com.atguigu.educms.pojo.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-25
 */
public interface CrmBannerService extends IService<CrmBanner> {

    //获取首页所有的banner
    List<CrmBanner> selectAllBanner();
}
