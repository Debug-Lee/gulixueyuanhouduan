package com.atguigu.eduorder.service;

import com.atguigu.eduorder.pojo.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-24
 */
public interface PayLogService extends IService<PayLog> {

    //生成二维码,返回二维码地址，还有其他相关数据
    Map createCode(String orderNo);

    //通过订单号查询订单状态
    Map queryOrderStatus(String orderNo);

    //添加订单到支付表，更新订单表支付状态
    void updateOrdersStatus(Map map);


}
