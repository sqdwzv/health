package cn.wangzhen.service;

import cn.wangzhen.domain.Permission;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.entity.Result;

public interface MaintainService {
    PageResult findPagePermission(QueryPageBean queryPageBean);

    void add(Permission permission);

    void delect(Integer id);

    Permission findById(Integer id);

    void edit(Permission permission);
}
