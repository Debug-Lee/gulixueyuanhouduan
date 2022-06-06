package com.atguigu.eduservice.service;

import com.atguigu.eduservice.pojo.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-03-28
 */
public interface EduTeacherService extends IService<EduTeacher> {

    // 分页查询讲师的方法
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> teacherPage);
}
