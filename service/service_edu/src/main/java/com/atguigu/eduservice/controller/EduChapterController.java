package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.pojo.EduChapter;
import com.atguigu.eduservice.pojo.chapter.ChapterVo;
import com.atguigu.eduservice.pojo.vo.CourseInfoVo;
import com.atguigu.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(description="课程章节管理")
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    //通过课程ID获取该课程中的章节和小节
    @GetMapping("getChapterVideo/{courseId}")
    @ApiOperation(value = "通过课程ID获取章节和小节")
    public R getChapterVideo(@PathVariable String courseId)
    {
        List<ChapterVo> list=eduChapterService.selectChapterByCourseId(courseId);
        return R.ok().data("AllChapterVideo",list);
    }

    //添加章节
    @PostMapping("addChapter")
    @ApiOperation(value = "添加章节")
    public R addChapter(@RequestBody EduChapter eduChapter)
    {
        boolean flag = eduChapterService.save(eduChapter);
        if(flag)
        {
            return R.ok();
        }else{
            return R.error();
        }
    }

    //根据章节ID查询章节
    @GetMapping("getChapterInfo/{chapterId}")
    @ApiOperation(value = "根据章节ID查询章节")
    public R getChapterInfo(@PathVariable String chapterId)
    {
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }



    //修改章节
    @PostMapping("updateChapter")
    @ApiOperation(value = "更新章节")
    public R updateChapter(@RequestBody EduChapter eduChapter)
    {
        boolean flag = eduChapterService.updateById(eduChapter);
        if(flag)
        {
            return R.ok();
        }else{
            return R.error();
        }
    }

    //根据章节ID删除章节
    @DeleteMapping("{chapterId}")
    @ApiOperation("根据章节Id删除章节")
    public R deleteChapter(@PathVariable String chapterId)
    {
        Boolean flag =eduChapterService.deleteChapter(chapterId);
        if(flag)
        {
            return R.ok();
        }else {
            return R.error();
        }
    }

}

