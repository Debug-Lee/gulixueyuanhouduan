package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.pojo.EduSubject;
import com.atguigu.eduservice.pojo.excel.excelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class subjectExcelListener extends AnalysisEventListener<excelSubjectData> {

    //因为SubjectExcelListener不能交给spring管理，需要自己new，不能注入其他对象
    //所以没办法自动注入service对象
    private EduSubjectService eduSubjectService;
    public subjectExcelListener() {
    }

    public subjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    //一行一行读取数据
    @Override
    public void invoke(excelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        if(excelSubjectData==null)
        {
            throw new GuliException(20001,"文件数据为空");
        }
        //判断一级分类不能重复添加
        //一行一行提取，一次读两个值,第一个值为一级分类，第二个值为二级分类
        EduSubject eduOneSubject = this.existOneSubject(eduSubjectService, excelSubjectData.getOneSubjectName());
        if(eduOneSubject ==null)//如果一级分类不存在，则进行添加
        {
            eduOneSubject=new EduSubject();
            eduOneSubject.setParentId("0");
            eduOneSubject.setTitle(excelSubjectData.getOneSubjectName());//设置一级分类名称
            eduSubjectService.save(eduOneSubject);
        }

        //判断二级分类不能重复添加
        //获取一级分类的ID值
        String pid=eduOneSubject.getId();
        EduSubject eduTwoSubject = this.existTwoSubject(eduSubjectService, excelSubjectData.getTwoSubjectName(),pid);
        if(eduTwoSubject ==null)//如果二级分类不存在，则进行添加
        {
            eduTwoSubject=new EduSubject();
            eduTwoSubject.setParentId(pid);
            eduTwoSubject.setTitle(excelSubjectData.getTwoSubjectName());//设置二级分类名称
            eduSubjectService.save(eduTwoSubject);
        }

    }
    //返回一个edusubject对象，看是否有多个，如果有多个则该一级分类已经存在
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name)
    {
        //select * from edu_subject where title='name' and parent_id=0
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",0);
        EduSubject subject = eduSubjectService.getOne(wrapper);
        return subject;
    }

    //返回一个edusubject对象，看是否有多个，如果有多个则该二级分类已经存在
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String pid)
    {
        //select * from edu_subject where title='name' and parent_id='pid'
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject subject = eduSubjectService.getOne(wrapper);
        return subject;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
