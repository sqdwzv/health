package cn.wangzhen.service;

import cn.wangzhen.domain.Permission;
import cn.wangzhen.domain.Role;
import cn.wangzhen.domain.User;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
     User user= userService.findByUsername(username);

     if (user==null){
         return null;
     }

        List<GrantedAuthority> list = new  ArrayList<>();

     //动态授权

        //获取角色
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //为用户授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));

            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //遍历权限,为用户授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
        return securityUser;
    }
}
