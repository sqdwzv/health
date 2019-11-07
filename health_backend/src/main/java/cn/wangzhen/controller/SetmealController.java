package cn.wangzhen.controller;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.constant.RedisConstant;
import cn.wangzhen.domain.Setmeal;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.SetmealService;
import cn.wangzhen.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/*体检套餐管理*/
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetmealService setmealService;

    //编辑
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.edit(setmeal,checkgroupIds);
            return new Result(true,"编辑套餐成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑套餐失败");
        }
    }

    //根据id查询套餐
    @RequestMapping("/findById")
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    public Result findById(int id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, "查询套餐成功", setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询套餐失败");
        }

    }

    //当前检查组包含所有检查项id,用于复选框选中状态
    @RequestMapping("/findCkeckgroupIdsBySetmealId")
    @PreAuthorize("hasAuthority('CHECKGROUP_QUERY')")
    public Result findCkeckgroupIdsBySetmealId(int id) {
        try {
            List<Integer> setmealIds = setmealService.findCkeckgroupIdsBySetmealId(id);
            System.out.println(setmealIds);
            return new Result(true, "查询数据成功", setmealIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "查询数据失败");
        }
    }

    //删除套餐
    @RequestMapping("/delect")
    @PreAuthorize("hasAuthority('SETMEAL_DELETE')")
    public Result delect(int id) {
        try {
            setmealService.delect(id);
            return new Result(true, "删除套餐组成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除套餐组失败");
        }
    }

    //分页
    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findPage(queryPageBean);
        System.out.println(pageResult.toString());
        return pageResult;
    }

    //文件上传
    @RequestMapping("/upload")
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();//原始文件名
        int lastIndexOf = originalFilename.lastIndexOf(".");
        String substring = originalFilename.substring(lastIndexOf - 1);//.jpg
        System.out.println(imgFile);
        String fileName = UUID.randomUUID().toString() + substring;//32为

        try {
            //将文件长传到7牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //给redis中存取文件名
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessagConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessagConstant.PIC_UPLOAD_SUCCESS, fileName);

    }

    //添加套餐
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
            return new Result(true, MessagConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessagConstant.ADD_SETMEAL_FAIL);
        }
    }
}
