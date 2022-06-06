package com.atguigu.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<DemoExcel> {
    //一行一行的读取数据
    @Override
    public void invoke(DemoExcel data, AnalysisContext analysisContext) {
        System.out.println(data);
    }

    //读取表头信息
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头"+headMap);
    }

    //读取完进行的操作
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
