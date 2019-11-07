package cn.wangzhen.service.impl;

import cn.wangzhen.dao.CheckItemDao;
import cn.wangzhen.domain.CheckItem;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
    //检查项分页查询
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //分页查询用插件
        PageHelper.startPage(currentPage,pageSize);

        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        PageResult pageResult = new PageResult(total, rows);
        return pageResult;
    }

    @Override
    public void delect(int id) {
        //先判断检查项在没在检查组中在删除
        Long count = checkItemDao.findCountByCheckItemId(id);
       if (count>0){
           throw new RuntimeException("当前检查项被引用,不能删除");
       }
       checkItemDao.delect(id);
    }

    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        Integer id = checkItem.getId();
        System.out.println(id);
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> checkItemList=checkItemDao.findAll();
        return checkItemList;
    }
}
