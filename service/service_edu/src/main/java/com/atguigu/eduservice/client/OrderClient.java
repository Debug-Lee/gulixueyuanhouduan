package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "service-order",fallback = OrderFileDegradeFeignClient.class)
public interface OrderClient {

    //3 根据订单号和用户ID查询订单订购状态
    @GetMapping("/eduorder/order/isBuyStatus/{courseId}/{memberId}")
    public Boolean isBuyStatus(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId);
}
