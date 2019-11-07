package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    /*编辑*/
    @RequestMapping("/edit")
    public Result edit(@RequestBody cn.wangzhen.domain.User user ,Integer[] checkitemIds) {
        try {
            userService.edit(user,checkitemIds);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            cn.wangzhen.domain.User user= userService.findById(id);
            return new Result(true,"查询回显成功",user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询回显失败");
        }
    }

    @RequestMapping("/getUsername")
    public Result getUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            System.out.println(user);
            String username = user.getUsername();
            return new Result(true, MessagConstant.GET_USERNAME_SUCCESS, username);
        }
        return new Result(false, MessagConstant.GET_USERNAME_FAIL);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return userService.findPage(queryPageBean);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody cn.wangzhen.domain.User user, Integer[] roleIds) {
        try {
            userService.add(user, roleIds);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("/delect")
    public Result delect(Integer id) {
        try {
            userService.delect(id);
            return new Result(true, "删除角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除角色失败");
        }
    }
    @RequestMapping("/findRoleIdsByUserId")
    public Result findRoleIdsByUserId(Integer id) {

        try {
            List<Integer> userIds = userService.findRoleIdsByUserId(id);
            return new Result(true,"查询角色id成功",userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询角色id失败");
        }

    }
    @RequestMapping("/getMenuList")
    public Result getMenuList(String username) {
        try {
            List<Map<Object,Object>> list =  userService.getMenuList(username);
            return new Result(true,"查询成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败");
        }

    }
    }
