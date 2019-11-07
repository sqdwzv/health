package cn.wangzhen.service.impl;

import cn.wangzhen.dao.RoleDao;
import cn.wangzhen.domain.Role;
import cn.wangzhen.service.RoleService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Override
    public List<Role> findAll() {
       List<Role> roles= roleDao.findAll();
        return roles;
    }
}
