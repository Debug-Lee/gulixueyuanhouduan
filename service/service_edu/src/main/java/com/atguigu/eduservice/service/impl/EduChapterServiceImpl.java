package com.atguigu.eduservice.service.impl;


import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.pojo.EduChapter;
import com.atguigu.eduservice.pojo.EduVideo;
import com.atguigu.eduservice.pojo.chapter.ChapterVo;
import com.atguigu.eduservice.pojo.chapter.VideoVo;
import com.atguigu.eduservice.pojo.vo.CourseInfoVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    //要在chapter里面操作video表，需要注入video的service
    @Autowired
    private EduVideoService eduVideoService;

    //通过课程ID获取该课程中的章节和小结
    @Override
    public List<ChapterVo> selectChapterByCourseId(String courseId) {
        //创建封装章节的list
        List<EduChapter> chapterList=new ArrayList<>();
        //创建封装小节的list
        List<EduVideo> videoList=new ArrayList<>();
        //创建最终的list
        List<ChapterVo> finalChapter=new ArrayList<>();

        //1.通过课程ID查询，得到章节的list
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        chapterList = baseMapper.selectList(chapterQueryWrapper);

        //2.通过课程ID查询，得到小节的list
        QueryWrapper<EduVideo> VideoQueryWrapper = new QueryWrapper<>();
        VideoQueryWrapper.eq("course_id",courseId);
        videoList = eduVideoService.list(VideoQueryWrapper);

        //3.循环遍历，封装章节
        for (int i = 0; i < chapterList.size(); i++) {
            //创建一个chapterVo对象装数据
            ChapterVo chapterVo = new ChapterVo();
            //获取chapterList中的一个值复制
            EduChapter eduChapter=chapterList.get(i);
            //将eduChapter中的数据复制到ChapterVo中
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //将chapterVo加入到finalList中
            finalChapter.add(chapterVo);
            //创建最终的小节list存放每个章节下的小节
            List<VideoVo> finalVideoVolist=new ArrayList<>();
        //4.循环遍历，封装小节
            for (int j = 0; j < videoList.size(); j++) {
                EduVideo eduVideo=new EduVideo();
                //获得每一个小节
                eduVideo=videoList.get(j);
                //判断当前小节是不是属于当前章节
                if(eduVideo.getChapterId().equals(eduChapter.getId()))
                {
                    //如果是，则创建videoVo对象，存储数据，并最终加入该最中小节的list
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    finalVideoVolist.add(videoVo);
                }
            }
            //设置章节的小节
            chapterVo.setChildren(finalVideoVolist);
        }
        return finalChapter;
    }

    //根据章节ID删除章节，如果章节里面有小节则不删除，如果没有小节则删除
    @Override
    public Boolean deleteChapter(String chapterId) {
        //先查询小节表中有无该章节，如果有，说明章节下面有小节，则不删除
        QueryWrapper<EduVideo> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("chapter_id",chapterId);
        int counts = eduVideoService.count(QueryWrapper);//count返回符合条件的数据数量，而不用获取这些数据

        if(counts>0)//如果小节中有该章节ID，则删除失败
        {
            throw new GuliException(20001,"删除失败");
        }else {
            int i = baseMapper.deleteById(chapterId);
            //i是删除的个数，如果删除成功，则1>0，返回true，如果删除失败0>0,返回false
            return i>0;
        }
    }


    //通过课程ID删除章节
    @Override
    public void deleteChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        baseMapper.delete(queryWrapper);
    }

}
