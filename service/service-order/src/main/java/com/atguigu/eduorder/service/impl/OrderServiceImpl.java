package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.CourseWebVoOrder;
import com.atguigu.commonutils.UcenterMemberVoOrder;
import com.atguigu.eduorder.client.CourseClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.pojo.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private UcenterClient ucenterClient;


    //生成订单，返回一个订单号
    @Override
    public String createOrders(String courseId, String memberId) {

        //远程调用，通过课程ID获取课程信息
        CourseWebVoOrder courseVo = courseClient.getCourseInfoOrder(courseId);
        //远程调用，通过用户ID获取用户信息
        UcenterMemberVoOrder ucenterVo = ucenterClient.getMemberInfoById(memberId);

        //创建一个order对象，进行赋值
        Order order = new Order();
        order.setGmtCreate(new Date());
        order.setGmtModified(new Date());
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseVo.getTitle());
        order.setCourseCover(courseVo.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseVo.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterVo.getMobile());
        order.setNickname(ucenterVo.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
