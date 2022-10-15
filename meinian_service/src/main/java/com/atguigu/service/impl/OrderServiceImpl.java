package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)//发布服务，注册到zk上了
@Transactional//声明式事务，所有方法都增加上事务
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderDao orderDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderSettingDao orderSettingDao;


    @Override
    public Result saveOrder(Map map) throws Exception {

//        1. 判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)

        //map里有传日期过来，但是object类型，转成String,然后用现成的日期函数工具类将字符串转成date类
        String orderDate =(String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);

        int setmealId = Integer.parseInt((String)map.get("setmealId"));

        //增加一个根据日期查询订单表的方法,如果查不出来，就不能预约
        OrderSetting orderSetting = orderSettingDao.getOrderSettingByOrderDate(date);
        if(orderSetting==null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }else{

//        2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
            int number = orderSetting.getNumber();//一共可以预约的人数
            int reservations = orderSetting.getReservations();//已经预约的人数
            if (reservations>=number){
                return new Result(false, MessageConstant.ORDER_FULL);
            }
        }


//        3. 判断是否是会员(根据手机号码查询t_member)
//                - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
//                - 如果不是会员(不能够查询出来) ,自动注册为会员(直接向t_member插入一条记录)
        String telephone =(String)map.get("telephone");
        Member member = memberDao.getMemberByTelephong(telephone);
        if(member==null){//不存在，快速注册，把map中的数据封装成一个member对象
            member = new Member();
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            member.setIdCard((String)map.get("idCard"));
            member.setPhoneNumber((String)map.get("telephone"));
            memberDao.add(member);//如果是快速注册要记得做主键回填，否则数据模型里是没有主键id的值的,在sql语句中keyProperty="id" useGeneratedKeys="true"
        }else{//检查是否重复预约，在订单表中，通过会员id，预约日期，预约的套餐id作为条件来查,看是否已经预约过了
            //这里演示的是把findOrderByCondition封装成一个通用的方法，可以实现动态SQL查询，及时通过别的条件组合也可以使用
            //将条件封装起来
            Order orderParam = new Order();
            orderParam.setOrderDate(date);
            orderParam.setMemberId(member.getId());
            orderParam.setSetmealId(setmealId);//上面第41行已经取到了这个套餐id

          List<Order> orderList = orderDao.findOrderByCondition(orderParam);

          if (orderList!=null && orderList.size()>0){
              return new Result(false, MessageConstant.HAS_ORDERED);
          }

        }

//        4.进行预约
//         - 向t_order表插入一条记录
//         - t_ordersetting表里面预约的人数reservations+1
//前面只要没有走return，就会继续走下去
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderType("微信预约");
        order.setOrderStatus("未出游");
        order.setSetmealId(setmealId);
        orderDao.add(order);//一定要主键回填,才可以在返回结果时拿到id，在sql语句中keyProperty="id" useGeneratedKeys="true"

        //将这个套餐的已预约人数加1，更新到数据库中
        orderSetting.setReservations(orderSetting.getReservations()+1);
        //为什么要传一个对象过去，因为到时要取对象里的参数作为查询条件，已经封装好了，大括号里就是对象的属性值
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById(Integer orderId) {
        //测试发现因为t_order这里面的日期是data类型，会有0分0秒，所以yong用日期工具类处理下
        try {
            Map map = orderDao.findById(orderId);
            Date date = (Date)map.get("orderDate");
            String strDate =DateUtils.parseDate2String(date);
            //再重新赋值map里面的orderDate
            map.put("orderDate",strDate);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
