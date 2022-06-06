package com.atguigu.eduservice.client;


import org.springframework.stereotype.Component;

@Component
public class OrderFileDegradeFeignClient implements  OrderClient{
    @Override
    public Boolean isBuyStatus(String courseId, String memberId) {
        return false;
    }
}
