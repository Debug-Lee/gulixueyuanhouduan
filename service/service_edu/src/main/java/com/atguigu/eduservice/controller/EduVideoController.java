package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.pojo.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo)
    {
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    //删除小节,同时删除视频
    @DeleteMapping("{id}")
    public R deleteVideo(@PathVariable String id)
    {
        EduVideo eduVideo = eduVideoService.getById(id);
        //获取视频ID
        String videoId = eduVideo.getVideoSourceId();
        //如果存在视频ID，则调用VOD服务中的方法对视频进行删除
        if(!StringUtils.isEmpty(videoId))
        {
        vodClient.removeAlyVideo(videoId);
        }
        eduVideoService.removeById(id);
        return R.ok();
    }

    //通过ID查询小节
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId)
    {
        EduVideo video=eduVideoService.getById(videoId);
        return R.ok().data("video",video);
    }

    //修改小节 TODO
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo)
    {
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    //
}

