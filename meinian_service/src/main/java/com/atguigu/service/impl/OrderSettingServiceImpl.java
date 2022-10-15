package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = OrderSettingService.class)//发布服务，注册到zk上了
@Transactional//声明式事务，所有方法都增加上事务
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void addBatch(ArrayList<OrderSetting> listData) {
        for (OrderSetting orderSetting : listData) {
            //如果导入的日期之前就存在，就更新数量值，而不是再新增一行，所以要做个判断
             int count = orderSettingDao.findOrderSettingByOrderDate(orderSetting.getOrderDate());
            if (count>0){
                orderSettingDao.edit(orderSetting);
            }else {
                orderSettingDao.add(orderSetting);
            }

        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String yeraMonth) {
        String startDate=yeraMonth+"-01";
        String endtDate=yeraMonth+"-31";
        //封装成一个map集合的参数类型
        Map parm = new HashMap();
        parm.put("startDate",startDate);
        parm.put("endtDate",endtDate);

        //就可以得到一个OrderSetting对象的集合,因为mybatis会自动封装成对象，但是返回给浏览器，接收的是集合数据，所以要转换
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(parm);

        //将实体对象的List集合结果转换成一个map类型的List集合，因为根据前台代码中，接收这个结果的leftobj对象，是map类型的
        List<Map> listDate = new ArrayList<>();



        for (OrderSetting orderSetting : list) {
            Map map  = new HashMap();

            /*this.leftobj = [
                        { date: 1, number: 120, reservations: 1 },
                        { date: 3, number: 120, reservations: 1 },
                        { date: 4, number: 120, reservations: 120 },
                        { date: 6, number: 120, reservations: 1 },
                        { date: 8, number: 120, reservations: 1 }
                    ];*/

            //看清楚，leftobj里的date只要返回日期就好了,不包括年月，获取日，用的是getDate()，而不是get(day)，这个要注意，前台要的数据一定要看清楚，要什么返回什么，比如这个如果返回年月日，页面显示不了数据的
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            listDate.add(map);
        }
        return listDate;
        }

    @Override
    public void editNumberByOrderDate(Map map) {
        try {
            String str = (String)map.get("orderDate");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(str);

            int count = orderSettingDao.findOrderSettingByOrderDate(date);

            OrderSetting orderSetting = new OrderSetting();
            orderSetting.setNumber(Integer.parseInt((String) map.get("value")));
            orderSetting.setOrderDate(date);

            //如果有设置这天的预约情况，就是更新操作，如果没有，就是添加操作,看清楚方法要传的参数，前面做些类型转换
            if(count>0){
                orderSettingDao.edit(orderSetting);
            }else{

                orderSettingDao.add(orderSetting);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}

