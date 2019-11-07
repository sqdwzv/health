package cn.wangzhen.service.impl;

import cn.wangzhen.constant.MessagConstant;
import cn.wangzhen.dao.MemberDao;
import cn.wangzhen.dao.OrderDao;
import cn.wangzhen.dao.OrderSettingDao;
import cn.wangzhen.domain.Member;
import cn.wangzhen.domain.Order;
import cn.wangzhen.domain.OrderSetting;
import cn.wangzhen.entity.Result;
import cn.wangzhen.service.OrderService;
import cn.wangzhen.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 * 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行 预约
 * 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
 * 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约 则无法完成再次预约
 * 4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注 册并进行预约
 * 5、预约成功，更新当日的已预约人数
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public Result order(Map map) throws Exception {
        //1.检查当日是否进行预约设置
        //获取当天时间
        String orderDate = (String) map.get("orderDate");
        //转换时间格式
        Date date = DateUtils.parseString2Date(orderDate);
        //是否可以预约
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting == null) {
            return new Result(false, MessagConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2.检查预约人数是否已满
        int number = orderSetting.getNumber();//总预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            return new Result(false, MessagConstant.ORDER_FULL);
        }
        //3.检查是否重复预约
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            //判断是否重复预约
            Integer memberId = member.getId();//会员id
            Date order_Date = DateUtils.parseString2Date(orderDate);//预约日期
            String setmealId = (String) map.get("setmealId");//套餐id

            Order order = new Order(memberId, order_Date, Integer.parseInt(setmealId));

            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size() > 0) {
                //已经进行预约,不能重复预约
                return new Result(false, MessagConstant.HAS_ORDERED);
            }
        } else {
            //4.检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注 册并进行预约
            member=new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String )map.get("idCard"));
            member.setSex((String)map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }

        //5.预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员id
        order.setOrderDate(DateUtils.parseString2Date(orderDate));//预约日期
        order.setOrderType((String) map.get("orderType"));//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        String setmealId = (String) map.get("setmealId");
        int setmealId2 = Integer.parseInt(setmealId);
        order.setSetmealId(setmealId2);//套餐id
        orderDao.add(order);

        //设置已预约人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        System.out.println(order.getId());
        return new Result(true,MessagConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(int id) throws Exception {
       Map map= orderDao.findById(id);
       if (map!=null&&map.size()>0){
           Date orderDate = (Date) map.get("orderDate");
           map.put("orderDate",DateUtils.parseDate2String(orderDate));
       }
        return map;
    }


}
