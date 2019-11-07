package cn.wangzhen.service.impl;

import cn.wangzhen.dao.MaintainDao;
import cn.wangzhen.domain.Permission;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.MaintainService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = MaintainService.class)
@Transactional
public class MaintainServiceImpl implements MaintainService {
    @Autowired
    private MaintainDao maintainDao;
    @Override
    public PageResult findPagePermission(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //分页查询用插件
        PageHelper.startPage(currentPage,pageSize);
      Page<Permission> page = maintainDao.selectByCondition(queryString);

        long total = page.getTotal();
        List<Permission> result = page.getResult();
        PageResult pageResult = new PageResult(total, result);
        return pageResult;
    }

    @Override
    public void add(Permission permission) {
        maintainDao.add(permission);
    }

    @Override
    public void delect(Integer id) {
        maintainDao.delect(id);
    }

    @Override
    public Permission findById(Integer id) {
        Permission permission = maintainDao.findById(id);
        return permission;
    }

    @Override
    public void edit(Permission permission) {
        maintainDao.edit(permission);
    }
}
