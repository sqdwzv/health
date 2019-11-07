package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.domain.Permission;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.MaintainService;
import com.alibaba.dubbo.config.annotation.Reference;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*动态维护*/
@RestController
@RequestMapping("/maintain")
public class MaintainController {
    @Reference
    private MaintainService maintainService;

    /*编辑*/
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission) {
        try {
            maintainService.edit(permission);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }


    /*回显*/
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Permission permission = maintainService.findById(id);
            return new Result(true, "回显成功", permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "回显失败");
        }
    }

    /*删除*/
    @RequestMapping("/delect")
    public Result delect(Integer id) {
        try {
            maintainService.delect(id);
            return new Result(true, "权限删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "权限删除失败");
        }
    }

    /*权限分页*/
    @RequestMapping("/findPagePermission")
    public PageResult findPagePermission(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = maintainService.findPagePermission(queryPageBean);
        return pageResult;
    }

    /*添加权限*/
    @RequestMapping("/add")
    public Result findPagePermission(@RequestBody Permission permission) {
        try {
            maintainService.add(permission);
            return new Result(true, "权限添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "权限添加失败");
        }

    }
}
