package cn.wangzhen.dao;

import cn.wangzhen.domain.CheckItem;
import cn.wangzhen.entity.Result;
import com.github.pagehelper.Page;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);
    Page<CheckItem> selectByCondition(String queryString);

    Long findCountByCheckItemId(int id);

    void delect(int id);

    CheckItem findById(int id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

}
