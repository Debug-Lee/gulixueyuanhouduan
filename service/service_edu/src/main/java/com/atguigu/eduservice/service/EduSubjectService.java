package com.atguigu.eduservice.service;

import com.atguigu.eduservice.pojo.EduSubject;
import com.atguigu.eduservice.pojo.subject.OneSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-05
 */
public interface EduSubjectService extends IService<EduSubject> {

    //导入Excel数据
    void importSubjectData(MultipartFile file, EduSubjectService eduSubjectService);

    //返回一级二级课程数据
    List<OneSubject> getAllOneAndTwo();
}
