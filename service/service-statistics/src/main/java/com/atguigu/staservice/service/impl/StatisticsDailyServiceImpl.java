package com.atguigu.staservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.pojo.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-30
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {


    @Autowired
    private UcenterClient ucenterClient;


    //统计某一天注册的人数
    @Override
    public void countRegisterDay(String day) {

        //添加之前先把当天的数据删除
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        //远程调用Ucent查询某一天注册的人数
        R registerR = ucenterClient.countRegister(day);
        Integer countRegister =(Integer) registerR.getData().get("countRegister");

        //向数据库中添加该天注册、登录、观看的数据
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setRegisterNum(countRegister); //当天注册的个数
        statisticsDaily.setDateCalculated(day); //添加某一天的数据
        statisticsDaily.setVideoViewNum(RandomUtils.nextInt(100,200));
        statisticsDaily.setCourseNum(RandomUtils.nextInt(100,200));
        statisticsDaily.setLoginNum(RandomUtils.nextInt(100,200));

        baseMapper.insert(statisticsDaily);
    }


    //图表显示，返回两个数据，日期json数组，数量json数组
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {

        //查询出所有的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);//查询这两个列
        List<StatisticsDaily> list = baseMapper.selectList(wrapper);

        //创建两个List用于封装两个数据
        List<String> date_calculated = new ArrayList<>();
        List<Integer> numDateList = new ArrayList<>();

        //循环遍历，将数据放入
        for (int i = 0; i < list.size(); i++) {
            StatisticsDaily daily = list.get(i);
            //封装日期List
            date_calculated.add(daily.getDateCalculated());

            //封装对应数量
            switch (type){
                case "login_num":
                    numDateList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDateList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDateList.add(daily.getVideoViewNum());
                    break;
                default:
                    numDateList.add(daily.getCourseNum());
                    break;
            }
        }
        //帮两个List放入map中返回
        HashMap<String, Object> result = new HashMap<>();
        result.put("date_calculated",date_calculated);
        result.put("numDateList",numDateList);

        return result;
    }
}
