package com.atguigu.educenter.mapper;

import com.atguigu.educenter.pojo.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-06
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //查询某一天注册的人数
    Integer countRegisterDay(String day);
}
