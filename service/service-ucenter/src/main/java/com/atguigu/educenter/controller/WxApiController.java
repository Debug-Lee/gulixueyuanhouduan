package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.pojo.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantUtil;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.exceptionHandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



import java.net.URLEncoder;
import java.util.HashMap;

@Controller  //因为不返回数据，所以使用controller
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    @GetMapping("callback")
    public String callback(String code,String state)
    {
        
        try {
            //第一步 获取code 临时票据

            //第二步 拿着code值 访问微信固定的地址，获取到两个值 access_token 和 openid
            // access_token 访问凭证  openid 每个微信的唯一标识
            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            //传入参数,生成访问微信的地址
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantUtil.WX_OPEN_APP_ID,
                    ConstantUtil.WX_OPEN_APP_SECRET,
                    code
            );
            //通过httpclient访问微信地址，获取两个值 access_token 和 openid
            String result = HttpClientUtils.get(accessTokenUrl);  //result是包含多个key-value的字符串

            //把result字符串中的 access_token 和 openid 提取出来
            //使用谷歌提供的gjson工具
            Gson gson = new Gson();
            //将字符串变为map，然后提取 access_token 和 openid
            HashMap map = gson.fromJson(result, HashMap.class);

            String accessToken =(String) map.get("access_token");
            String openId =(String) map.get("openid");



            //添加数据到数据库
            //根据OpenId判断数据库有没有当前数据
            UcenterMember member = memberService.getByOpenId(openId);
            if(member == null)
            {
                //第三步 再次访问微信服务器，获取用户信息

                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";

                //填入参数
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openId);

                //使用httpclient访问地址，获取信息
                String userInfo = HttpClientUtils.get(userInfoUrl);
                HashMap userMap = gson.fromJson(userInfo, HashMap.class);
                String nickname =(String) userMap.get("nickname");
                String headimgurl =(String) userMap.get("headimgurl");

                member = new UcenterMember();
                member.setNickname(nickname);
                member.setOpenid(openId);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            //使用jwt工具根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后：返回首页面，通过路径传递token字符串
            return "redirect:http://localhost:3000?token="+jwtToken;
        }catch (Exception e)
        {
            throw  new GuliException(20001,"登录失败");
        }

    }

    @GetMapping("login")
    public String getWxCode(){

        // 微信开放平台授权baseUrl
        //调用微信，传入公司参数，获取到二维码
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对重定向地址进行utf-8 编码
        String redirectUrl = ConstantUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        }catch (Exception e){

        }

        //调用String方法，在%s处传入参数
        String url = String.format(baseUrl,
                ConstantUtil.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu"
        );

        return "redirect:"+ url;
    }
}
