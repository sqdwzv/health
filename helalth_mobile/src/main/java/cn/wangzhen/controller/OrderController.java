package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.constant.RedisMessageConstant;
import cn.wangzhen.domain.Order;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.OrderService;
import cn.wangzhen.utils.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.rmi.MarshalException;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Map map =    orderService.findById(id);
            return new Result(true,MessagConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.QUERY_ORDER_FAIL);
        }
    }

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        //获取键 手机号+编号
        String telephone = (String) map.get("telephone");
        //从redis中获取验证码
        String codeInRedid = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //获取客户输入验证码进行比对
        String validateCode = (String) map.get("validateCode");
        Result result = null;
        if (codeInRedid != null && validateCode != null && codeInRedid.equals(validateCode)) {
            //存储短信类型
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //成功进行预约
            try {
                result  = orderService.order(map);//dubbo远程调用服务
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            if (result.isFlag()){
                String orderDate = (String) map.get("orderDate");
                //预约成功给客户发送短信说明预约成功
                try {
                  /*  SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);*/
                    System.out.println("预约成功,在"+orderDate);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Result(false,MessagConstant.ORDER_FAIL);
                }
            }
            return result;

        }else {  //失败进行返回结果给页面
            return new Result(false,MessagConstant.VALIDATECODE_ERROR);
        }
    }
}
