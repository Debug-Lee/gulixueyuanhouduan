package com.atguigu.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.eduorder.pojo.Order;
import com.atguigu.eduorder.pojo.PayLog;
import com.atguigu.eduorder.mapper.PayLogMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-24
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    //生成二维码,返回二维码地址，还有其他相关数据
    @Override
    public Map createCode(String orderNo) {

        try {
            //1.通过订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order order = orderService.getOne(wrapper);

            //2.将信息封装到Map中,设置二维码需要的参数
            Map m =new HashMap();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle());   //订单标题
            m.put("out_trade_no", orderNo);     //订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n");
            m.put("trade_type", "NATIVE");

            //3.创建client，封装为xml格式文件发送请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //4.接受返回的数据，进行最后封装到map中返回
            //返回第三方数据
            String xml = client.getContent();
            Map<String,String> resultMap = WXPayUtil.xmlToMap(xml);

            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);
            return map;

        }catch (Exception e)
        {
            throw new GuliException(20001,"生成二维码失败");
        }

    }


    //通过订单号查询订单状态
    @Override
    public Map queryOrderStatus(String orderNo) {

        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        }catch (Exception e)
        {
            throw new GuliException(20001,"查询订单状态出错！");
        }
    }


    //添加订单到支付表，更新订单表支付状态
    @Override
    public void updateOrdersStatus(Map map) {

        //获取订单id
        String orderNo = (String) map.get("out_trade_no");

        //1.通过订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(wrapper);

        //如果订单号已经支付，则返回
        if(order.getStatus().intValue() == 1) {return ;}
        order.setStatus(1);
        orderService.updateById(order);

        //向支付表中添加数据
        PayLog p = new PayLog();
        p.setOrderNo(order.getOrderNo());//支付订单号
        p.setPayTime(new Date());//支付时间
        p.setPayType(1);//支付类型
        p.setTotalFee(order.getTotalFee());//总金额(分)
        p.setTradeState((String) map.get("trade_state"));//支付状态
        p.setTransactionId((String) map.get("transaction_id"));//流水号
        p.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(p);

    }
}
