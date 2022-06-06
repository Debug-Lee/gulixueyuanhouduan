package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-24
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成二维码,返回二维码地址，还有其他相关数据
    //orderNo参数为订单号
    @GetMapping("createCode/{orderNo}")
    public R createCode(@PathVariable String orderNo)
    {
        Map map = payLogService.createCode(orderNo);
        System.out.println("生成二维码========="+map);
        return R.ok().data(map);
    }

    //通过订单号查询订单状态
    //如果订单已支付，则添加一条新纪录到pay log里面
    @GetMapping("queryOrderStatus/{orderNo}")
    public R queryOrderStatus(@PathVariable String orderNo)
    {
        Map map = payLogService.queryOrderStatus(orderNo);
        System.out.println("更新订单========="+map);
        if(map == null) //如果返回为空，则订单出错
        {
            return R.error().message("订单出错了");
        }
        if(map.get("trade_state").equals("SUCCESS")) //支付状态成功
        {
            //添加订单到支付表，更新订单表支付状态
            payLogService.updateOrdersStatus(map);
            return R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");
    }

}

