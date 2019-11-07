package cn.wangzhen.dao;

import cn.wangzhen.domain.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    Map findById(int id);

    List<Order> findByCondition(Order order);

    void add(Order order);

    Integer findOrderCountByDate(String tody);

    Integer findOrderCountAfterDate(String thisWeekMonday);

    Integer findVisitsCountByDate(String tody);

    Integer findVisitsCountAfterDate(String thisWeekMonday);

    List<Map> findHotSetmeal();

}
