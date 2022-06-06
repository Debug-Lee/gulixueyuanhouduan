package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.pojo.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    //通过课程ID删除小节，同时删除视频
    @Override
    public void deleteByCourseId(String courseId) {

        //根据课程ID查询所有的视频ID
        QueryWrapper<EduVideo> videoQueryWrapper=new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseId);
        videoQueryWrapper.select("video_source_id");  //设置查询出只有 video_source_id字段的数据
        List<EduVideo> eduVideos = baseMapper.selectList(videoQueryWrapper);
        //将list<eduVdieo> 转变为 list<String>
        List<String> videoIds=new ArrayList<>();
        for (int i = 0; i < eduVideos.size(); i++) {
            EduVideo video = eduVideos.get(i);
            String videoId = video.getVideoSourceId();
            if(!StringUtils.isEmpty(videoId))
            {
                videoIds.add(videoId);
            }
        }
        //如果视频ID集有数，则远程调用删除多个视频
        if(videoIds.size()>0)
        {
            vodClient.deleteBatch(videoIds);
        }

        QueryWrapper<EduVideo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        baseMapper.delete(queryWrapper);
    }
}
