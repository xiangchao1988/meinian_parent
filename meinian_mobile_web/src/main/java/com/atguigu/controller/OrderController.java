package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/order")//这个父路径来源于前端代码中页面发起的请求的地址
public class OrderController {

    @Reference
    OrderService orderService;

    @RequestMapping("/saveOrder")
    //因为页面orderInfo数据模型传过来的数据包含会员，订单等多张表，pojo里没有包含这么多属性的模型对象，会接收不完整，所以用万能容器Map来接收
    public Result saveOrder(@RequestBody Map map){
        try {
            System.out.println("map"+map);
            //这里要获取填入的手机号码map.get("telephone")和验证码map.get("validateCode")，比对reids里存的数据是否一致jedispool.getResource().get....
            //验证码发送有问题，没有装redis,这里没有做验证码校验

            Result result=orderService.saveOrder(map);
            return  result;

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }


    /**
     *<p>会员姓名：{{orderInfo.member}}</p>
     *<p>旅游套餐：{{orderInfo.setmeal}}</p>
     *<p>旅游日期：{{orderInfo.orderDate}}</p>
     *<p>预约类型：{{orderInfo.orderType}}</p>
     * @param id 订单id
     * @return
     * 因为需要返回的数据来源于三张表，没法用现有的实体对象接收，所以用万能参数Map来接收
     */
    @RequestMapping("/findById")
    public Result findById(Integer orderId){
        Map map= orderService.findById(orderId);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
    }

}
