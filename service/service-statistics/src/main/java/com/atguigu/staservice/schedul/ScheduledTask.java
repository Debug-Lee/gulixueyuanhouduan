package com.atguigu.staservice.schedul;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.uitils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService staService;

    /**
     * 测试
     * 每天七点到二十三点每五秒执行一次
     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1() {
//        System.out.println("*********++++++++++++*****执行了");
//    }

    /**
     * 每天将前一天的数据添加到数据库中
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void task2(){
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        staService.countRegisterDay(day);
    }

}
