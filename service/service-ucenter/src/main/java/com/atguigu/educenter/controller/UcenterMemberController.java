package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.UcenterMemberVoOrder;
import com.atguigu.educenter.pojo.UcenterMember;
import com.atguigu.educenter.pojo.vo.RegisterVo;
import com.atguigu.educenter.pojo.vo.UcenterMemberVo;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author BuleFlat
 * @since 2022-05-06
 */
@RestController
@RequestMapping("/educenter/member")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    //登录的方法
    @PostMapping("login")
    public R login(@RequestBody UcenterMember member)
    {
        //meber用户账号和密码
        //调用service实现登录
        //返回token值，使用jwt生成
        String token = ucenterMemberService.login(member);
        return R.ok().data("token",token);
    }

    //注册的方法
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo)
    {
        ucenterMemberService.register(registerVo);
        return  R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request)
    {
        //调用jwt工具类的方法，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据用户id获取用户信息
        UcenterMember ucenterMember = ucenterMemberService.getById(memberId);
        return R.ok().data("userInfo",ucenterMember);

    }

    //根据用户ID查询用户信息
    @PostMapping("getMemberInfoById/{id}")
    public UcenterMemberVoOrder getMemberInfoById(@PathVariable String id)
    {
        UcenterMember ucenterMember =  ucenterMemberService.getById(id);
        UcenterMemberVoOrder memberVo = new UcenterMemberVoOrder();
        BeanUtils.copyProperties(ucenterMember,memberVo);
        return memberVo;
    }

    //查询某一天注册的人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day)
    {
        Integer countRegister = ucenterMemberService.countRegisterDay(day);
        return R.ok().data("countRegister",countRegister);
    }

}

