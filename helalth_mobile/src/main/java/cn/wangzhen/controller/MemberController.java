package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.constant.RedisMessageConstant;
import cn.wangzhen.domain.Member;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;
    /*手机号快速登录*/
    @RequestMapping("/login")
    public Result login(HttpServletResponse response , @RequestBody Map map){
        /**
         * 1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
         * 2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
         * 3、向客户端写入Cookie，内容为用户手机号
         * 4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
         */
        //1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        String validateCode = (String) map.get("validateCode");//前端端验证码
        String telephone = (String) map.get("telephone");//前端手机号
        //获取redis中的验证码进行比对
        String codeInRedis  = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (codeInRedis==null || !codeInRedis.equals(validateCode)){
            //验证码验证错误
            return new Result(false, MessagConstant.VALIDATECODE_ERROR);
        }else {
            //验证码匹配,判断是否为会员
          Member member =   memberService.findByTelephone(telephone);
          if (member==null){
              //不是会员注册会员
              Member member1 = new Member();
              member.setPhoneNumber(telephone);
              member.setRegTime(new Date());
              memberService.add(member1);
          }
            //登录成功       
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);
            //将会员信息保存到redis中
            String  json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessagConstant.LOGIN_SUCCESS);
        }
    }
}
