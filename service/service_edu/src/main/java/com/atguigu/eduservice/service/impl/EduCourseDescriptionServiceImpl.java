package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.pojo.EduCourseDescription;
import com.atguigu.eduservice.mapper.EduCourseDescriptionMapper;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
@Service
public class EduCourseDescriptionServiceImpl extends ServiceImpl<EduCourseDescriptionMapper, EduCourseDescription> implements EduCourseDescriptionService {


    //通过课程ID删除课程描述
    @Override
    public void deleteDesByCourseId(String courseId) {
        baseMapper.deleteById(courseId);
    }
}
