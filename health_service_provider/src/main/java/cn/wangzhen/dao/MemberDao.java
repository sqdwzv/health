package cn.wangzhen.dao;

import cn.wangzhen.domain.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    Member findByTelephone(String telephone);

    void add(Member member);

    Integer findMemberCountByMonth(String month);

    Integer findMemberCountByDate(String tody);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);

    List<Map<String, Object>> findMemberCount();

}
