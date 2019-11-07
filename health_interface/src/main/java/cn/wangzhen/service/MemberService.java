package cn.wangzhen.service;

import cn.wangzhen.domain.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member1);

    List<Integer> findMemberCountByMonth(List<String> months);

    List<Map<String, Object>> findMemberCount();


}
