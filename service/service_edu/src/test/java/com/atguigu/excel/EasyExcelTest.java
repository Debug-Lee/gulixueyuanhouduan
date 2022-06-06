package com.atguigu.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class EasyExcelTest {
    public static void main(String[] args) {
        //Excel写操作
        //1.设置写入文件的目录和文件名称
//        String filename="D:\\Code\\student.xlsx";

        //2.调用Excel写入方法
//        EasyExcel.write(filename,DemoExcel.class).sheet("学生列表").doWrite(getList());

        //Excel都操作
        //1.设置读入的文件
        String filename="D:\\Code\\student.xlsx";

        //2.调用Excel读方法
        EasyExcel.read(filename,DemoExcel.class,new ExcelListener()).sheet().doRead();
    }

    public static List<DemoExcel> getList()
    {
        List<DemoExcel> list=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoExcel demoExcel = new DemoExcel();
            demoExcel.setSname("lucy"+i);
            demoExcel.setSno(i);
            list.add(demoExcel);
        }
        return list;
    }
}
