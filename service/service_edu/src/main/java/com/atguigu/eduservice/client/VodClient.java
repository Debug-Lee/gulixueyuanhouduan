package com.atguigu.eduservice.client;


import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="service-vod",fallback = VodFileDegradeFeignClient.class)  //调用的服务名称
@Component
public interface VodClient {

    //定义调用的方法路径，全路径
    //@PathVariable注解一定要指定参数名称，否则出错
    //@PathVariable用于属性前，指定当前属性的值用URL中某个值进行赋值
    //通过视频ID删除阿里云视频
    @DeleteMapping("/eduvod/video/removeAlyVideo/{videoId}")
    public R removeAlyVideo(@PathVariable("videoId") String videoId);

    //通过视频ID删除多个阿里云视频
    @DeleteMapping("/eduvod/video/deleteBatch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
