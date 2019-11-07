package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.domain.CheckGroup;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;
    //查询所有选项组
    @RequestMapping("/findAll")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    public Result delect() {
        try {
            List<CheckGroup> checkGroups= checkGroupService.findAll();
            return new Result(true,MessagConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.QUERY_CHECKGROUP_FAIL);
        }

    }
    //删除选项组
    @RequestMapping("/delect")
    @PreAuthorize("hasAuthority('CHECKGROUP_DELETE')")
    public Result delect(Integer id) {
        try {
            checkGroupService.delect(id);
            return new Result(true,MessagConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.DELETE_CHECKGROUP_FAIL);
        }

    }
    //修改选项组  编辑
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKGROUP_EDIT')")
    public Result edit(@RequestBody CheckGroup checkGroup ,Integer[] checkitemIds) {
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true,MessagConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.EDIT_CHECKGROUP_FAIL);
        }

    }
    //查询单个检查组
    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        try {
            List<Integer> checkItemIds= checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true,MessagConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessagConstant.QUERY_CHECKITEM_FAIL);
        }
    }
    //查询单个检查组
    @RequestMapping("/findById")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    public Result findById(Integer id) {
        CheckGroup checkGroup = checkGroupService.findById(id);
        if (checkGroup != null) {
            return new Result(true,MessagConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }
        return new Result(false,MessagConstant.QUERY_CHECKGROUP_FAIL);
    }
    //检查组分页
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.findPage(queryPageBean);
        return pageResult;
    }
    //添加检查组
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessagConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessagConstant.ADD_CHECKGROUP_SUCCESS);
    }
}
