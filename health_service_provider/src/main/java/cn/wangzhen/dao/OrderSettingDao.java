package cn.wangzhen.dao;

import cn.wangzhen.domain.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    Long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting datum);

    void add(OrderSetting datum);

    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    OrderSetting findByOrderDate(Date date);

    void editReservationsByOrderDate(OrderSetting orderSetting);
}
