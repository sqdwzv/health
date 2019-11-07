package cn.wangzhen.dao;

import cn.wangzhen.domain.Role;

import java.util.List;
import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(int id);

    List<Role> findAll();

}
