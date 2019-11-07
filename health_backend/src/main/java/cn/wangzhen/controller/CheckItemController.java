package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.domain.CheckItem;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/*检查项管理*/
@Controller
@ResponseBody
/*@RestController*/
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;
    //查询所有检查项
    @RequestMapping("/findAll")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findAll() {
        List<CheckItem> checkItemList = checkItemService.findAll();
        if (checkItemList != null && checkItemList.size()>0){
            return new Result(true,MessagConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        }
        return new Result(false,MessagConstant.QUERY_CHECKITEM_FAIL);
    }

    //添加体检项
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            return new Result(false, MessagConstant.ADD_CHECKITEM_FAIL);
        }
        Result result = new Result(true, MessagConstant.ADD_CHECKITEM_SUCCESS);
        System.out.println(result);
        return result;
    }

    //检查项分页查询
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }

    //根据id删除检查项
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/delect")
    public Result delect(int id) {
        try {
            checkItemService.delect(id);
        } catch (Exception e) {
            return new Result(false, MessagConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessagConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //修改回显
    @RequestMapping("/findById")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result findById(int id) {
        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, MessagConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        } catch (Exception e) {
            return new Result(false, MessagConstant.QUERY_CHECKITEM_FAIL);
        }
    }
    //编辑
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            return new Result(false,MessagConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessagConstant.EDIT_CHECKITEM_SUCCESS);
    }
}