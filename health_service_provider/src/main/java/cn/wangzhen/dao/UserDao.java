package cn.wangzhen.dao;

import cn.wangzhen.domain.Menu;
import cn.wangzhen.domain.Role;
import cn.wangzhen.domain.User;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface UserDao {
    User findByUsername(String username);

    Page<User> findPage(String queryString);


    void add(User user);

    void setRoleAndUser(Map<String, Integer> map);

    void delectAssociation(Integer id);

    void delect(Integer id);

    User findById(Integer id);

    List<Integer> findRoleIdsByUserId(Integer id);

    void edit(User user);

    Integer findRoleIdsByUsername(String username);

    List<Menu> findMenuByRoleId(Integer roleId);

    List<Menu> findMenuById(Integer id);
}
