package cn.wangzhen.service;

import cn.wangzhen.domain.Setmeal;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    void delect(int id);

    Setmeal findById(int id);

    List<Integer> findCkeckgroupIdsBySetmealId(int id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> findAll();

    List<Map<String, Object>> findSetmealCount();
}
