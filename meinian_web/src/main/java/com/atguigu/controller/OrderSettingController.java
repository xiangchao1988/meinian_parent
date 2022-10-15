package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import com.atguigu.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderSetting")//对应实体类的名字，首字母小写
public class OrderSettingController {
    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/editNumberByOrderDate")
    //Map是万能的，用Map来接收param的数据，因为是请求体传过来的，记得注释
    public Result editNumberByOrderDate(@RequestBody Map map){
        try {
            orderSettingService.editNumberByOrderDate(map);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String yeraMonth){
        //获取的预约信息最终要赋给leftobj，而leftobj是下面这种Map格式的数据，就是key-value这种类型，所以List集合泛型是Map
        /*this.leftobj = [
                        { date: 1, number: 120, reservations: 1 },
                        { date: 3, number: 120, reservations: 1 },
                        { date: 4, number: 120, reservations: 120 },
                        { date: 6, number: 120, reservations: 1 },
                        { date: 8, number: 120, reservations: 1 }
                    ];*/
        //前台要拿到Result里的data数据赋值给leftobj集合，所以结果要生成一个List<map>集合
        List<Map> list = orderSettingService.getOrderSettingByMonth(yeraMonth);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,list);
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile)  {//工具类中需要的参数就是这个类型，名称和前台代码传的名称保持一致
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);//获得了一个list集合，每一条数据是一个String[]集合

            ArrayList<OrderSetting> listData = new ArrayList<>();//将前面获得的list集合中的数据转换为OrderSetting对象的集合

            for (String[] strArray : list) {
                //获取每一行的两个参照
                String dateStr =strArray[0];
                String numberStr=strArray[1];
                //新建一个OrderSetting对象，设置对象的属性
                OrderSetting orderSetting = new  OrderSetting();
                orderSetting.setOrderDate(new Date(dateStr));
                orderSetting.setNumber(Integer.parseInt(numberStr));
                //讲对象放到listData集合中
                listData.add(orderSetting);
            }
            //通过上面的操作就把数据拿到了，然后就是远程调用业务逻辑了
            //controller层就可以理解为将页面传过来的数据拿到，调用业务层方法的作用
            orderSettingService.addBatch(listData);
            //如果这些代码都成功，就说明数据到后台了，就提示上次成功
            return new Result(true, MessageConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.UPLOAD_FAIL);
        }
    }
}
