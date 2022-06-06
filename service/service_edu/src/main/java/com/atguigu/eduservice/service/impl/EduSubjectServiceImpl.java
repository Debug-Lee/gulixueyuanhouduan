package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.listener.subjectExcelListener;
import com.atguigu.eduservice.pojo.EduSubject;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.pojo.excel.excelSubjectData;
import com.atguigu.eduservice.pojo.subject.OneSubject;
import com.atguigu.eduservice.pojo.subject.TwoSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-04-05
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //导入Excel数据
    @Override
    public void importSubjectData(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            InputStream inputStream = file.getInputStream();//用输入流输入文件
            EasyExcel.read(inputStream, excelSubjectData.class,new subjectExcelListener(eduSubjectService)).sheet().doRead();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //返回一级二级课程数据
    @Override
    public List<OneSubject> getAllOneAndTwo() {
        //创建一级分类对象list
        List<EduSubject> oneSubject=new ArrayList<>();
        //创建二级分类对象list
        List<EduSubject> twoSubject=new ArrayList<>();
        //创建最终list，用于封装数据
        List<OneSubject> finalSubjectList=new ArrayList<>();

        //1.查询所有一级分类  parent_id=0
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");//baseMapper可以直接在service中进行查询数据
        List<EduSubject> eduOneSubjects = baseMapper.selectList(wrapperOne);

        //2.查询所有二级分类  parent_id!=0
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperOne.ne("parent_id","0");
        List<EduSubject> eduTwoSubjects = baseMapper.selectList(wrapperTwo);

        //3.封装一级分类
        for (int i = 0; i < eduOneSubjects.size(); i++) {
            EduSubject eduSubject=eduOneSubjects.get(i);
            OneSubject subject = new OneSubject();
//            subject.setId(eduSubject.get(i).getId());
//            subject.setTitle(eduSubject.get(i).getTitle());
            BeanUtils.copyProperties(eduSubject,subject);//可以实现两个类相同属性的复制
            finalSubjectList.add(subject);

            //4.封装二级分类
            List<TwoSubject> finalTwoSubject=new ArrayList<>();//存放每个一级分类下，所有的二级分类
            //循环遍历二级list，找到属于该次循环下一级分类的二级分类
            for (int j = 0; j < eduTwoSubjects.size(); j++) {
                EduSubject tSubject = eduTwoSubjects.get(j);
                //如果当前二级分属于该一级分类，则添加进去
                if(tSubject.getParentId().equals(subject.getId()))//判断当前二级分类的父亲是否为当前一级分类，是则添加
                {
                    TwoSubject twoSubject2 = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject2);
                    finalTwoSubject.add(twoSubject2);
                }
            }
            subject.setChildren(finalTwoSubject);//将最后的二级分类封装到一级分类中
        }
        return finalSubjectList;
    }


}
