package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.pojo.UcenterMember;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.pojo.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.exceptionHandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-06
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    //因为要调用redis中的验证码，注入redis操作对象
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //登录的方法
    @Override
    public String login(UcenterMember member) {

        //取出手机号和密码
        String phone = member.getMobile();
        String code = member.getPassword();

        //判断账号密码是否为空，如果为空，则返回错误
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(code))
        {
            throw new GuliException(20001,"登录失败");
        }

        //通过手机号查询数据库中数据，返回对象
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",phone);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        //如果查询对象为空，则说明不存在，登录失败
        if(ucenterMember == null)
        {
            throw new GuliException(20001,"登录失败");
        }

        //验证密码
        //密码需要MD5加密再作比较，因为数据库存储的密码是由MD5加密过后的
        if (!MD5.encrypt(code).equals(ucenterMember.getPassword()))
        {
            throw new GuliException(20001,"登录失败");
        }

        //验证用户是否被禁用
        if(ucenterMember.getIsDisabled())
        {
            throw new GuliException(20001,"登录失败");
        }

        //登陆成功，以jwt规则生成token并返回
        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
        return jwtToken;
    }

    //注册的方法
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册信息
        String nickname=registerVo.getNickname();//昵称
        String mobile = registerVo.getMobile();//手机号
        String password = registerVo.getPassword();//密码
        String code = registerVo.getCode();//验证码

        //非空判断
        if(StringUtils.isEmpty(nickname) || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(password))
        {
            throw new GuliException(20001,"注册失败");
        }

        //验证码判断
        String redisCode=redisTemplate.opsForValue().get(mobile);
        if(!code.equals(redisCode))
        {
            throw new GuliException(20001,"注册失败");
        }

        //查询数据库中当前手机号是否唯一，如果有值则注册失败
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0)
        {
            throw new GuliException(20001,"注册失败");
        }

        //将数据添加到数据库中
        UcenterMember ucenterMember = new UcenterMember();
        ucenterMember.setNickname(nickname);
        ucenterMember.setMobile(mobile);
        ucenterMember.setPassword(MD5.encrypt(password));//将密码用MD5加密存入数据库中
        ucenterMember.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        ucenterMember.setIsDisabled(false);
        baseMapper.insert(ucenterMember);

    }

    //根据OpenId判断数据库有没有当前数据
    @Override
    public UcenterMember getByOpenId(String openId) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openId);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    //查询某一天注册的人数
    @Override
    public Integer countRegisterDay(String day) {
        Integer countDay = baseMapper.countRegisterDay(day);
        return countDay;
    }
}
