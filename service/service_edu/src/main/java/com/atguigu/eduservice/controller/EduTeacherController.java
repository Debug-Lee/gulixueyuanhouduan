package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.pojo.EduTeacher;
import com.atguigu.eduservice.pojo.vo.teacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.atguigu.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Service;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author BuleFlat
 * @since 2022-03-28
 */
@Api(description="讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    /**
     * 查询所有的数据
     * http://localhost:8001/eduservice/teacher/findAll
     */
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R selectAll()
    {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list); //链式编程   R.ok().data()   调用Ok返回对象，在调用方法data传入数据
    }

    /**
     * 通过ID进行逻辑删除
     */
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("{id}")
    public R deleteById(@ApiParam(name = "id", value = "讲师ID", required = true)@PathVariable String id)
    {
        boolean flag = teacherService.removeById(id);
        if(flag)
        {
            return R.ok();
        }else
        {
            return R.error();
        }
    }

    /**
     * 分页讲师列表
     */
    @ApiOperation(value = "分页讲师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageTeacher(@ApiParam(name = "current", value = "当前页", required = true)@PathVariable long current,
                         @ApiParam(name = "limit", value = "每页显示条数", required = true)@PathVariable Long limit)
    {
        Page<EduTeacher> pageTeacher=new Page<>(current,limit);
        //调用方法的时候，底层会将所有的数据封装到page对象里面
        teacherService.page(pageTeacher,null);

        try {
            int i=10/0;
        }catch (Exception e)
        {
            throw new GuliException(20001,"进行自定义异常处理....");
        }

        Long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> list =pageTeacher.getRecords();//每页显示的个数

        return R.ok().data("total",total).data("items",list);//链式进行数据的设置
    }


    /**
     * 多条件查询分页讲师列表
     */
    @ApiOperation(value = "多条件分页查询讲师")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current,
                                  @PathVariable long limit,
                                  @RequestBody(required = false) teacherQuery teacherQuery)
    {
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);

        //加入wrapper条件,new 一个 QueryWrapper
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();

        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //拼接sql语句,column属性为表中的属性名称
        if(!StringUtils.isEmpty(name))//如果该属性非空，则拼接字符串
        {
            queryWrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level))
        {
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin))
        {
            queryWrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end))
        {
            queryWrapper.le("gmt_modified",end);
        }

        //排序
        queryWrapper.orderByDesc("gmt_create");


        //调用service.page方法
        teacherService.page(pageTeacher,queryWrapper);

        Long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> list =pageTeacher.getRecords();//每页显示的个数

        return R.ok().data("total",total).data("rows",list);//链式进行数据的设置
    }


    /**
     * 添加讲师接口
     */
    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher)
    {
        boolean save = teacherService.save(eduTeacher);
        if(save)
        {
            return R.ok();
        }else
        {
            return R.error();
        }
    }


    /**
     * 根据ID查询讲师
     */
    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("getTeacher/{id}")
    public R getTeacherById(@PathVariable String id)
    {
        EduTeacher edu = teacherService.getById(id);
        return R.ok().data("teacher",edu);
    }

    /**
     * 根据ID修改讲师
     */
    @ApiOperation(value = "根据ID修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacherById(@RequestBody EduTeacher eduTeacher)
    {
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag)
        {
            return R.ok();
        }else
        {
            return R.error();
        }
    }


}

