package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.pojo.EduCourse;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.pojo.EduCourseDescription;
import com.atguigu.eduservice.pojo.EduTeacher;
import com.atguigu.eduservice.pojo.frontvo.CourseVo;
import com.atguigu.eduservice.pojo.frontvo.CourseWebVo;
import com.atguigu.eduservice.pojo.vo.CourseInfoVo;
import com.atguigu.eduservice.pojo.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //注入描述service
    @Autowired
    EduCourseDescriptionService eduCourseDescriptionService;

    //注入小节service
    @Autowired
    EduVideoService eduVideoService;
    //注入章节service
    @Autowired
    EduChapterService eduChapterService;
    //添加课程以及简介
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        //1.向edu_course表中给添加课程
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert == 0)
        {
            throw new GuliException(20001,"课程信息保存失败！");
        }

        //获取该课程的id，实现课程与简介一对一
        String uid=eduCourse.getId();

        //2.向edu_course_description表中添加简介
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescription.setId(uid);//设置描述的id和课程id一样
        boolean save = eduCourseDescriptionService.save(eduCourseDescription);
        if(!save)
        {
            throw new GuliException(20001,"课程简介保存失败！");
        }
        return uid;
    }

    //根据课程ID查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        //查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //查询描述表
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());
        return courseInfoVo;
    }


    //修改课程信息
    @Override
    public void updateById(CourseInfoVo courseInfoVo) {

        //修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int rows = baseMapper.updateById(eduCourse);
        if(rows==0)
        {
            throw new GuliException(20001,"修改课程信息失败！");
        }

        //修改描述表
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        boolean flag = eduCourseDescriptionService.updateById(eduCourseDescription);
        if(!flag)
        {
            throw  new GuliException(20001,"修改课程简介失败！");
        }

    }

    //通过课程ID查询课程最终发布信息
    @Override
    public CoursePublishVo getPublishCourseInfo(String courseId) {

        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(courseId);
        return publishCourseInfo;
    }


    //根据课程ID删除课程
    @Override
    public void deleteCourseById(String courseId) {

        //1.删除小节
        eduVideoService.deleteByCourseId(courseId);

        //2.删除章节
        eduChapterService.deleteChapterByCourseId(courseId);

        //3.删除描述
        eduCourseDescriptionService.deleteDesByCourseId(courseId);

        //4.删除课程
        int rows = baseMapper.deleteById(courseId);
        if(rows == 0)
        {
            throw new GuliException(20001,"删除课程失败！");
        }
    }


    // 分页条件查询课程
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> coursePage, CourseVo courseVo) {

        //加入查找条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(courseVo.getSubjectParentId())) //判断一级标题条件有无
        {
            wrapper.eq("subject_parent_id",courseVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseVo.getSubjectId())) //判断二级标题条件有无
        {
            wrapper.eq("subject_id",courseVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseVo.getBuyCountSort())) //关注度
        {
            wrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(courseVo.getGmtCreateSort())) //时间
        {
            wrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(courseVo.getPriceSort())) //价格
        {
            wrapper.orderByDesc("price");
        }
        
        baseMapper.selectPage(coursePage,wrapper);//查询的数据封装到coursePage中

        //将所有的数据取出来
        List<EduCourse> records = coursePage.getRecords();
        long current = coursePage.getCurrent();
        long pages = coursePage.getPages();
        long size = coursePage.getSize();
        long total = coursePage.getTotal();
        boolean hasNext = coursePage.hasNext();
        boolean hasPrevious = coursePage.hasPrevious();

        //将所有的数据放入map中
        Map<String,Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    //根据ID,编写sql，查询基本信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }


}
