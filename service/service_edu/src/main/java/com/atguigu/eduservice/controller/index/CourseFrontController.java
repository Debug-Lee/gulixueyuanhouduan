package com.atguigu.eduservice.controller.index;


import com.atguigu.commonutils.CourseWebVoOrder;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.OrderClient;
import com.atguigu.eduservice.pojo.EduCourse;
import com.atguigu.eduservice.pojo.EduTeacher;
import com.atguigu.eduservice.pojo.chapter.ChapterVo;
import com.atguigu.eduservice.pojo.frontvo.CourseVo;
import com.atguigu.eduservice.pojo.frontvo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "前台课程管理")
@CrossOrigin
@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;


    // 分页条件查询课程
    @PostMapping("getCourseFrontList/{page}/{limit}")
    @ApiOperation("分页条件查询讲师")
    public R getTeacherFrontList(@PathVariable long page,@PathVariable long limit,
                                 @RequestBody(required = false) CourseVo courseVo)
    {
        Page<EduCourse> coursePage = new Page<>(page,limit);
        Map<String,Object> map = courseService.getCourseFrontList(coursePage,courseVo);
        //返回分页的所有数据
        return R.ok().data(map);
    }

    //根据课程ID查询课程详情
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request)
    {
        //根据ID,编写sql，查询基本信息
        CourseWebVo webVo = courseService.getBaseCourseInfo(courseId);

        //根据ID查询章节和小节
        List<ChapterVo> list = chapterService.selectChapterByCourseId(courseId);

        //根据课程ID和用户ID查询订单状态
        Boolean isBuy = orderClient.isBuyStatus(courseId, JwtUtils.getMemberIdByJwtToken(request));

        return R.ok().data("course",webVo).data("chapterList",list).data("isBuy",isBuy);
    }

    //根据课程ID查询课程信息
    @PostMapping("getCourseInfoOrder/{courseId}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String courseId)
    {
        CourseWebVo courseVo = courseService.getBaseCourseInfo(courseId);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseVo,courseWebVoOrder);
        return courseWebVoOrder;
    }




}
