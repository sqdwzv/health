package cn.wangzhen.service.impl;

import cn.wangzhen.dao.MemberDao;
import cn.wangzhen.domain.Member;
import cn.wangzhen.service.MemberService;
import cn.wangzhen.utils.MD5Utils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {

        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member1) {
        String password = member1.getPassword();
        if (password != null) {
            password = MD5Utils.md5(password);//将明文密码进行加密
            member1.setPassword(password);
        }
        memberDao.add(member1);
    }

    //根据月份统计会员数量
    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list = new ArrayList<>();
        for (String month : months) {
            month = month + ".31";
            Integer count = memberDao.findMemberCountByMonth(month);
            list.add(count);
        }
        return list;
    }
    /*性别占比统计*/
    @Override
    public List<Map<String, Object>> findMemberCount() {


        List<Map<String, Object>> memberCount = memberDao.findMemberCount();
        for (Map<String, Object> map : memberCount) {
            String name = (String) map.get("name");
            if (name.equals("1")){
               name="男";
           }else {
                name="女";
            }
            map.put("name",name);
        }
        return  memberCount;

    }
}
