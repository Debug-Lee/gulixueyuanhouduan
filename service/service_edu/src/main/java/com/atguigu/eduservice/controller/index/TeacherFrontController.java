package com.atguigu.eduservice.controller.index;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.pojo.EduCourse;
import com.atguigu.eduservice.pojo.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "前台讲师管理")
@CrossOrigin
@RestController
@RequestMapping("/eduservice/teacherfront")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    // 分页查询讲师的方法
    @GetMapping("getTeacherFrontList/{page}/{limit}")
    @ApiOperation("分页查询讲师")
    public R getTeacherFrontList(@PathVariable long page,@PathVariable long limit)
    {
        Page<EduTeacher> teacherPage = new Page<>(page,limit);
        Map<String,Object> map = teacherService.getTeacherFrontList(teacherPage);
        //返回分页的所有数据
        return R.ok().data(map);
    }

    //查询讲师详细信息的方法
    @GetMapping("getTeacherInfo/{id}")
    @ApiOperation("根据ID获取讲师详细信息")
    public R getTeacherInfo(@PathVariable String id)
    {
        //获取讲师信息
        EduTeacher eduTeacher = teacherService.getById(id);
        //获取讲师所讲课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",id);
        List<EduCourse> courseList = courseService.list(wrapper);

        return R.ok().data("teacher",eduTeacher).data("courseList",courseList);
    }


}
