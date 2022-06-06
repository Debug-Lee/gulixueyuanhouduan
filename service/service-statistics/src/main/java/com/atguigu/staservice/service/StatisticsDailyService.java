package com.atguigu.staservice.service;

import com.atguigu.staservice.pojo.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-30
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    //统计某一天注册的人数
    void countRegisterDay(String day);

    //图表显示，返回两个数据，日期json数组，数量json数组
    Map<String, Object> getShowData(String type, String begin, String end);
}
