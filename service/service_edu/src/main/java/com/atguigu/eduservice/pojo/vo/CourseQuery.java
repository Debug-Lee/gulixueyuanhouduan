package com.atguigu.eduservice.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CourseQuery {
    @ApiModelProperty(value = "课程标题,模糊查询")
    private String title;

    @ApiModelProperty(value = "状态 已发布 未发布")
    private String status;

}
