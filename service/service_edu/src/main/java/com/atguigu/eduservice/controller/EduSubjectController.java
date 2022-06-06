package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.pojo.subject.OneSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;


import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-05
 */
@Api(description = "课程分类管理")
@RestController
@RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    @ApiOperation(value = "Excel文件读操作")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file)
    {
        //1.获取上传的excel文件 MultipartFile
        eduSubjectService.importSubjectData(file,eduSubjectService);
        //2.判断返回集合是否为空
        return R.ok();
    }

    @ApiOperation(value = "嵌套数据列表")
    @GetMapping("getAllSubject")
    public R listSubject(){
        List<OneSubject>list=eduSubjectService.getAllOneAndTwo();
        return R.ok().data("list",list);
    }
}

