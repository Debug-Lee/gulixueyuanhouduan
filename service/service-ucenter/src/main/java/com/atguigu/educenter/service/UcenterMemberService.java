package com.atguigu.educenter.service;

import com.atguigu.educenter.pojo.UcenterMember;
import com.atguigu.educenter.pojo.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-06
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //登录的方法
    String login(UcenterMember member);

    //注册的方法
    void register(RegisterVo registerVo);

    //根据OpenId判断数据库有没有当前数据
    UcenterMember getByOpenId(String openId);

    //查询某一天注册的人数
    Integer countRegisterDay(String day);
}
