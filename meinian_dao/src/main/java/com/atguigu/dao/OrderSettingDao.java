package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    void add(OrderSetting orderSetting);



    void edit(OrderSetting orderSetting);

    int findOrderSettingByOrderDate(Date orderDate);

    List<OrderSetting> getOrderSettingByMonth(Map parm);


    OrderSetting getOrderSettingByOrderDate(Date date);


    void editReservationsByOrderDate(OrderSetting orderSetting);
}
