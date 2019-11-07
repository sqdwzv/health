package cn.wangzhen.dao;

import cn.wangzhen.domain.CheckGroup;
import cn.wangzhen.domain.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);
    void setChetGroupAndItem(Map map);

    Page<CheckItem> selectByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void delectAssociation(Integer id);

    void edit(CheckGroup checkGroup);

    void delect(Integer id);

    List<CheckGroup> findAll();

}
