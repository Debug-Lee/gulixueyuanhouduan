package com.atguigu.staservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.staservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-30
 */
@RestController
@RequestMapping("/staservice/sta")
@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService staService;

    //统计某一天注册的人数
    @PostMapping("countRegisterDay/{day}")
    public R countRegisterDay(@PathVariable String day)
    {
        staService.countRegisterDay(day);
        return R.ok();
    }

    //图表显示，返回两个数据，日期json数组，数量json数组
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type,@PathVariable String begin,
                      @PathVariable String end){

        Map<String,Object> map = staService.getShowData(type,begin,end);
        return R.ok().data(map);
    }

}

