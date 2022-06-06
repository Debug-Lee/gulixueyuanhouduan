package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.pojo.EduCourse;
import com.atguigu.eduservice.pojo.frontvo.CourseWebVo;
import com.atguigu.eduservice.pojo.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    //通过课程Id查询最终的课程信息
    public CoursePublishVo getPublishCourseInfo(String courseId);

    //根据ID,编写sql，查询基本信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
