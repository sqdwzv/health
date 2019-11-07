package cn.wangzhen.service;

import cn.wangzhen.domain.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(List<OrderSetting> data);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
