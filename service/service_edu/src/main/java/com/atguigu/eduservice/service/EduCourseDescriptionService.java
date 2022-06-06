package com.atguigu.eduservice.service;

import com.atguigu.eduservice.pojo.EduCourseDescription;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
public interface EduCourseDescriptionService extends IService<EduCourseDescription> {

    //通过课程ID删除课程描述
    void deleteDesByCourseId(String courseId);
}
