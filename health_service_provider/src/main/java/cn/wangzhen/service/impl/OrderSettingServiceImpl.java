package cn.wangzhen.service.impl;

import cn.wangzhen.dao.OrderSettingDao;
import cn.wangzhen.domain.OrderSetting;
import cn.wangzhen.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> data) {
        if (data != null && data.size() > 0) {
            for (OrderSetting datum : data) {
                //判断当前日期是否已经进行了预约设置
                Long countByOrderDate = orderSettingDao.findCountByOrderDate(datum.getOrderDate());
                if (countByOrderDate > 0) {
                    // 已经存在，执行更新操作
                    orderSettingDao.editNumberByOrderDate(datum);
                } else {
                    //不存在,进行添加操作
                    orderSettingDao.add(datum);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {//2019-3
        Map<String, String> map = new HashMap<String, String>();
        String dateBegin = date + "-1";//2019-3-1
        String dateEnd = date + "-31";//2019-3-31
        map.put("dateBegin", dateBegin);
        map.put("dateEnd", dateEnd);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        if (list!=null && list.size()>0) {
            for (OrderSetting orderSetting : list) {
                Map orderSettingMap = new HashMap();
                Date date1 = orderSetting.getOrderDate();
                Calendar instance = Calendar.getInstance();//当前时间
                instance.setTime(date1);//设置指定时间

                orderSettingMap.put("date", instance.get(Calendar.DAY_OF_MONTH));//获取具体几号
                orderSettingMap.put("number", orderSetting.getNumber());//获取一共课预约人数
                orderSettingMap.put("reservations", orderSetting.getReservations());
                data.add(orderSettingMap);
            }
        }
        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {

        Long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (countByOrderDate>0){
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }

    }
}
