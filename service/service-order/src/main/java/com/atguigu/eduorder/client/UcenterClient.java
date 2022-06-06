package com.atguigu.eduorder.client;

import com.atguigu.commonutils.UcenterMemberVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient(name = "service-ucenter")
public interface UcenterClient {

    //根据用户ID查询用户信息
    @PostMapping("/educenter/member/getMemberInfoById/{id}")
    public UcenterMemberVoOrder getMemberInfoById(@PathVariable("id") String id);
}
