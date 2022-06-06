package com.atguigu.eduservice.pojo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class excelSubjectData {

    //value设置写入Excel表中的属性列,index设置读入的列对应类中的属性
    @ExcelProperty(value = "一级分类",index=0)
    private String oneSubjectName;

    @ExcelProperty(value = "二级分类",index=1)
    private String twoSubjectName;
}
