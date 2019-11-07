package cn.wangzhen.dao;

import cn.wangzhen.domain.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String, Integer> map);

    Page<Setmeal> findPage(String queryString);

    void delect(int id);

    void del(int id);

    Setmeal findById(int id);

    List<Integer> findCkeckgroupIdsBySetmealId(int id);

    void edit(Setmeal setmeal);

    List<Setmeal> findAll();

    List<Map<String, Object>> findSetmealCount();


}
