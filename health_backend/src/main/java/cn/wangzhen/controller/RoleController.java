package cn.wangzhen.controller;

import cn.wangzhen.domain.Role;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.RoleService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Role> roles= roleService.findAll();
            return new Result(true,"查询所有角色成功",roles);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询所有角色失败");
        }
    }

}
