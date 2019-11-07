package cn.wangzhen.dao;

import cn.wangzhen.domain.Permission;
import com.github.pagehelper.Page;

public interface MaintainDao {

    Page<Permission> selectByCondition(String queryString);

    void add(Permission permission);

    void delect(Integer id);

    Permission findById(Integer id);

    void edit(Permission permission);
}
