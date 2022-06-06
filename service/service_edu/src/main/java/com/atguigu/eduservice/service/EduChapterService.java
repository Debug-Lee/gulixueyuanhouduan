package com.atguigu.eduservice.service;

import com.atguigu.eduservice.pojo.EduChapter;
import com.atguigu.eduservice.pojo.chapter.ChapterVo;
import com.atguigu.eduservice.pojo.vo.CourseInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
public interface EduChapterService extends IService<EduChapter> {

    //通过课程ID获取该课程中的章节和小结
    List<ChapterVo> selectChapterByCourseId(String courseId);

    //根据章节ID删除章节
    Boolean deleteChapter(String chapterId);

    //通过课程ID删除章节
    void deleteChapterByCourseId(String courseId);
}
