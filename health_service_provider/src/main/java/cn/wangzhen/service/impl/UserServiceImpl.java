package cn.wangzhen.service.impl;

import cn.wangzhen.dao.PermissionDao;
import cn.wangzhen.dao.RoleDao;
import cn.wangzhen.dao.UserDao;
import cn.wangzhen.domain.*;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.service.UserService;
import cn.wangzhen.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //根据用户名查询用户信息和关联的角色信息,同时需要查询角色关联的权限信息
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);//用户基本信息
        if (user == null) {
            return null;
        }
        Integer userId = user.getId();
        //根据userId查询role
        Set<Role> roles = roleDao.findByUserId(userId);
        if (roles != null && roles.size() > 0) {
            //将roles赋值给user
            user.setRoles(roles);
            for (Role role : roles) {
                Integer roleId = role.getId();
                //根据roleId查询相关permission
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                if (permissions != null && permissions.size() > 0) {
                    role.setPermissions(permissions);//给角色赋权限
                }
            }
            user.setRoles(roles);//给用户赋角色
        }
        return user;
    }

    /*查询所有用户信息分页*/
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        Integer currentPage = queryPageBean.getCurrentPage();
        //分页插件
        PageHelper.startPage(currentPage, pageSize);
        Page<User> page = userDao.findPage(queryString);
        List<User> result = page.getResult();
        long total = page.getTotal();
        PageResult pageHelper = new PageResult(total, result);
        return pageHelper;
    }

    @Override
    public void add(User user, Integer[] roleIds) {
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        //新增用户
        userDao.add(user);
        if (roleIds != null && roleIds.length > 0) {
            //添加关系表
            for (Integer roleId : roleIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("userId", user.getId());
                map.put("roleId", roleId);
                userDao.setRoleAndUser(map);
            }
        }

    }

    @Override
    public void delect(Integer id) {
        //删除中间表
        userDao.delectAssociation(id);
        //删除用户
        userDao.delect(id);
    }

    @Override
    public User findById(Integer id) {
        User user = userDao.findById(id);
        return user;
    }

    @Override
    public List<Integer> findRoleIdsByUserId(Integer id) {
        List<Integer> userIds = userDao.findRoleIdsByUserId(id);
        return userIds;
    }

    @Override
    public void edit(User user, Integer[] checkitemIds) {
        //删除中间表数据
        userDao.delectAssociation(user.getId());
        //向中间表添加新的关系
        if (checkitemIds != null && checkitemIds.length > 0) {
            //添加关系表
            for (Integer roleId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("userId", user.getId());
                map.put("roleId", roleId);
                userDao.setRoleAndUser(map);
            }
        }
        //修改用户表
        userDao.edit(user);
    }

    /*根据角色动态展示列表*/
    @Override
    public List<Map<Object, Object>> getMenuList(String username) {
        //查出他的角色id
        Integer roleId = userDao.findRoleIdsByUsername(username);
        //根据角色id查菜单
        List<Menu> menuList = userDao.findMenuByRoleId(roleId);
        List list = new ArrayList();
        for (Menu menu : menuList) {
            /* {
                "path": "1",
                "title": "工作台",
                "icon":"fa-dashboard",
                "children": []
            }
         */
            Map map= new HashMap();

            String path = menu.getPath();
            map.put("path",path);
            String name = menu.getName();
            map.put("title",name);
            String icon = menu.getIcon();
            map.put("icon",icon);

            //根据父id查询子菜单
            Integer id = menu.getId();
           List<Menu> list2= userDao.findMenuById(id);
           List list3 = new ArrayList();
            for (Menu menu1 : list2) {
                Map map1 = new HashMap();
                String path1 = menu1.getPath();
                map1.put("path",path1);
                String name1 = menu1.getName();
                map1.put("title",name1);
                String linkUrl = menu1.getLinkUrl();
                map1.put("linkUrl",linkUrl);
                list3.add(map1);
            }
            map.put("children",list3);
            list.add(map);
        }

        return list;
    }
}
