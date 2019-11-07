package cn.wangzhen.service.impl;

import cn.wangzhen.dao.CheckGroupDao;
import cn.wangzhen.domain.CheckGroup;
import cn.wangzhen.domain.CheckItem;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    //新增检查组同时关联检查项
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        //新增检查组
        checkGroupDao.add(checkGroup);
        //设置多对多关联 t_checkgroup_checkItem
        Integer checkGroupId = checkGroup.getId();
        setCheckGroupAndCheckItem(checkGroupId, checkItemIds);
    }

    //向中间表(t_checkgroup_checkitem)插入数据（建立检查组和检查项关联关系） 抽取
    public void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkItemIds) {
        if (checkItemIds != null && checkItemIds.length > 0) {
            for (Integer checkItemId : checkItemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkItemId", checkItemId);
                map.put("checkGroupId", checkGroupId);
                checkGroupDao.setChetGroupAndItem(map);
            }
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();

        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);

    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    //修改
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //清除中间表关系
        checkGroupDao.delectAssociation(checkGroup.getId());
        //向中间表添加新的关系
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
        //更新检查组基本信息
        checkGroupDao.edit(checkGroup);
    }

    @Override
    public void delect(Integer id) {
        //先删除关系表再删除选项组
        checkGroupDao.delectAssociation(id);
        //删除选项组
        checkGroupDao.delect(id);
    }

    @Override
    public List<CheckGroup> findAll() {

     return    checkGroupDao.findAll();
    }
}
