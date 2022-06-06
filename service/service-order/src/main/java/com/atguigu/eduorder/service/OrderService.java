package com.atguigu.eduorder.service;

import com.atguigu.eduorder.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-24
 */
public interface OrderService extends IService<Order> {

    //生成订单，返回一个订单号
    String createOrders(String courseId, String memberIdByJwtToken);
}
