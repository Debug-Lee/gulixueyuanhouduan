package com.atguigu.eduservice.service;

import com.atguigu.eduservice.pojo.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-08
 */
public interface EduVideoService extends IService<EduVideo> {

    //通过课程ID删除小节
    void deleteByCourseId(String courseId);


}
