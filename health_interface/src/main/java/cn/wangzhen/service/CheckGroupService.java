package cn.wangzhen.service;

import cn.wangzhen.domain.CheckGroup;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup,Integer[] checkItemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void delect(Integer id);

    List<CheckGroup> findAll();
}
