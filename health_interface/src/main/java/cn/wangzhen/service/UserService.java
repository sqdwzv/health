package cn.wangzhen.service;

import cn.wangzhen.domain.Role;
import cn.wangzhen.domain.User;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;

import java.util.List;
import java.util.Map;

public interface UserService {
    User findByUsername(String username);

    PageResult findPage(QueryPageBean queryPageBean);

    void add(User user,Integer[] roleIds);

    void delect(Integer id);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void edit(User user, Integer[] checkitemIds);

    List<Map<Object, Object>> getMenuList(String username);
}
