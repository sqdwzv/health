package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.domain.Setmeal;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    //查询所有套餐
    @RequestMapping("/getAllSetmeal")
    public Result getSetmeal(){
        try {
            List<Setmeal> list= setmealService.findAll();
            return new Result(true, MessagConstant.GET_SETMEAL_LIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.GET_SETMEAL_LIST_FAIL);
        }
    }
    //根据id查询单个套餐详情
    @RequestMapping("/findById")
    public Result findById(int id){
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessagConstant.GET_SETMEAL_LIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.GET_SETMEAL_LIST_FAIL);
        }
    }
}
