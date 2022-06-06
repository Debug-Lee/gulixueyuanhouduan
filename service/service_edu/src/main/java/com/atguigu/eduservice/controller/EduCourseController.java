package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.pojo.EduCourse;
import com.atguigu.eduservice.pojo.EduTeacher;
import com.atguigu.eduservice.pojo.vo.CourseInfoVo;
import com.atguigu.eduservice.pojo.vo.CoursePublishVo;
import com.atguigu.eduservice.pojo.vo.CourseQuery;
import com.atguigu.eduservice.pojo.vo.teacherQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
@Api(description="课程管理")
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation(value = "增加课程信息")
    @PostMapping("/addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        //返回课程ID,便于后续页面数据的添加
        String id=eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }


    //根据课程ID查询课程基本信息
    @GetMapping("/getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId)
    {
        CourseInfoVo courseInfoVo=eduCourseService.getCourseInfo(courseId);
        return R.ok().data("courseInfoVo",courseInfoVo);
    }

    //修改课程信息
    @PostMapping("/updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo)
    {
        eduCourseService.updateById(courseInfoVo);
        return R.ok();
    }

    //通过课程ID查询课程最终发布信息
    @GetMapping("getPublishCourse/{courseId}")
    public R getPublishCourse(@PathVariable String courseId)
    {
        CoursePublishVo coursePublishVo=eduCourseService.getPublishCourseInfo(courseId);
        return R.ok().data("coursePublish",coursePublishVo);
    }

    //通过课程ID对课程进行发布
    @PostMapping("publishCourse/{courseId}")
    public R publishCourse(@PathVariable String courseId)
    {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    //获取所有的课程
    @GetMapping("getAllCourseList")
    public R getAllCourseList()
    {
        List<EduCourse> list = eduCourseService.list(null);
        return R.ok().data("list",list);
    }

    /**
     * 多条件查询分页课程列表
     */
    @ApiOperation(value = "多条件分页查询讲师")
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) CourseQuery courseQuery)
    {
        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current,limit);

        //加入wrapper条件,new 一个 QueryWrapper
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();

        String title =courseQuery.getTitle();
        String status=courseQuery.getStatus();
        //拼接sql语句,column属性为表中的属性名称
        if(!StringUtils.isEmpty(title))//如果该属性非空，则拼接字符串
        {
            queryWrapper.like("title",title);
        }
        if(!StringUtils.isEmpty(status))
        {
            queryWrapper.eq("status",status);
        }

        //排序
        queryWrapper.orderByDesc("gmt_create");


        //调用service.page方法
        eduCourseService.page(pageCourse,queryWrapper);

        Long total = pageCourse.getTotal();//总记录数
        List<EduCourse> list =pageCourse.getRecords();//每页显示的个数

        return R.ok().data("total",total).data("rows",list);//链式进行数据的设置
    }

    /**
     * 根据课程ID删除课程
     */
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId)
    {
        eduCourseService.deleteCourseById(courseId);
        return R.ok();
    }

}

