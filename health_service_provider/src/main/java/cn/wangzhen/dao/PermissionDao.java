package cn.wangzhen.dao;

import cn.wangzhen.domain.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(int roleId);
}
