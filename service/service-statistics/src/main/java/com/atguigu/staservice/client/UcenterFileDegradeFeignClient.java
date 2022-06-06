package com.atguigu.staservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

@Component
public class UcenterFileDegradeFeignClient implements UcenterClient{
    @Override
    public R countRegister(String day) {
        return R.error().message("删除视频失败了");
    }
}
