package cn.wangzhen.service.impl;

import cn.wangzhen.constant.RedisConstant;
import cn.wangzhen.dao.SetmealDao;
import cn.wangzhen.domain.Setmeal;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.service.SetmealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //先添加套餐组
        setmealDao.add(setmeal);
        setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        String img = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }
    //分页
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
       Page<Setmeal> setmealPage= setmealDao.findPage(queryString);
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }

    @Override
    public void delect(int id) {
        //删除关系表
        setmealDao.del(id);
        //删除套餐表
        setmealDao.delect(id);
    }

    @Override
    public Setmeal findById(int id) {
        return   setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCkeckgroupIdsBySetmealId(int id) {
        List<Integer> ckeckgroupIdsBySetmealId = setmealDao.findCkeckgroupIdsBySetmealId(id);
        System.out.println(ckeckgroupIdsBySetmealId);
        return ckeckgroupIdsBySetmealId;
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //先删除中间表
        setmealDao.del(setmeal.getId());
        //添加中间表
       setSetmealAndCheckGroup( setmeal.getId(),checkgroupIds);
       //修改套餐表
        setmealDao.edit(setmeal);
    }

    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> list=setmealDao.findAll();
        return list;
    }

    //添加套餐组和选项关系组中间表
    public void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        //在添加关系表
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("checkgroupId", checkgroupId);
                map.put("setmealId", id);
                setmealDao.setSetmealAndCheckGroup(map);
            }
        }
    }
    //查询套餐预约占比数据
    @Override
    public List<Map<String, Object>> findSetmealCount() {


        return setmealDao.findSetmealCount();
    }
}
