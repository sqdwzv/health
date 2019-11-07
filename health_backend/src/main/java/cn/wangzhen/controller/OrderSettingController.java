package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.domain.OrderSetting;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.OrderSettingService;
import cn.wangzhen.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;
    //根据日期修改可预约人数
    @RequestMapping("/editNumberByDate")
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessagConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.ORDERSETTING_FAIL);
        }
    }
    //查询excel数据
    @RequestMapping("/getOrderSettingByMonth")
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    public Result getOrderSettingByMonth(String date){//参数格式2019-03
        try {
            List<Map> list=  orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessagConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.GET_ORDERSETTING_FAIL);
        }
    }
    //上传excel数据
    @RequestMapping("/upload")
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    public Result upload(@RequestParam("excelFile")MultipartFile excelFile){
        try {
            //解析表格数据存储到集合中
            List<String[]> strings = POIUtils.readExcel(excelFile);
            List<OrderSetting> data= new  ArrayList<OrderSetting>();
            for (String[] string : strings) {
                String orderDate = string[0];
                String number = string[1];
                OrderSetting orderSetting = new OrderSetting(new Date(orderDate), Integer.parseInt(number));
                data.add(orderSetting);
            }
            //通过dubbo远程调用服务实现数据批量导入到数据库
            orderSettingService.add(data);
            return new Result(true, MessagConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            //文件解析失败
            return  new Result(false,MessagConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }
}
