package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.constant.RedisMessageConstant;
import cn.wangzhen.entity.Result;
import cn.wangzhen.utils.SMSUtils;
import cn.wangzhen.utils.ValidateCodeUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    /* @Reference
     private ValidateCodeService validateCodeService;*/
    @Autowired
    private JedisPool jedisPool;

    //体检预约时发送短信验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //生成随机4位数验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        try {
            //发送验证码
            /* SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());*/
            System.out.println(validateCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessagConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的验证码是" + validateCode);
        //将验证码存入redis中
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5000, validateCode.toString());
        return new Result(true, MessagConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //随机生成4位验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //发送验证码
        try {
           /* SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,validateCode.toString());*/
            System.out.println("快速登录验证码!:"+validateCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码存入jedis中
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,5000,validateCode.toString());
        return new Result(true,MessagConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
