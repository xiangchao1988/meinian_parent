package com.atguigu.service;

import com.atguigu.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void addBatch(ArrayList<OrderSetting> listData);

    List<Map> getOrderSettingByMonth(String yeraMonth);

    void editNumberByOrderDate(Map map);
}
