package com.atguigu.eduservice.service;

import com.atguigu.eduservice.pojo.EduCourse;
import com.atguigu.eduservice.pojo.frontvo.CourseVo;
import com.atguigu.eduservice.pojo.frontvo.CourseWebVo;
import com.atguigu.eduservice.pojo.vo.CourseInfoVo;
import com.atguigu.eduservice.pojo.vo.CoursePublishVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程以及简介
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程ID查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);

    //修改课程信息
    void updateById(CourseInfoVo courseInfoVo);

    //通过课程ID查询课程最终发布信息
    CoursePublishVo getPublishCourseInfo(String courseId);

    //根据课程ID删除课程
    void deleteCourseById(String courseId);

    // 分页条件查询课程
    Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseVo courseVo);

    //根据ID,编写sql，查询基本信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
