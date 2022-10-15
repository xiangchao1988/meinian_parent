package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")//这个父路径来源于前端代码中页面发起的请求的地址
public class AddressController {
    @Reference
    AddressService addressService;

    @RequestMapping("/findAllMaps")
    public Map findAllMaps(){
        Map map = new HashMap();//看前台代码要求，是要返回一个data，观察data值发现是一个包含gridnMaps和nameMaps两个键的map集合，
        //再观察发现gridnMaps和nameMaps有长度，所以是两个List数组，又因为它们可以点出lng、lat、addressName这些属性，所以List数
        // 组存着map集合，通过键值对存放地址名称和经纬度信息,分析完数据结构，代码就可以倒着逐步写出来了

        List<Address> list =addressService.findAllMaps();//查询结果是封装到Address实体对象中

        List<Map> nameMaps = new ArrayList<>();//放地址名称
        List<Map> gridnMaps = new ArrayList<>();//放经纬度

        for (Address address : list) {
            String addressName = address.getAddressName();//取到了地址值，因为地址都是放在List集合里的每个map集合键值对，所以要有个map来装遍历的每个地址键值对，然后再添加到地址List集合中
            Map<String,String> nameMap = new HashMap<>();
            nameMap.put("addressName",addressName);
            nameMaps.add(nameMap);

            Map<String,String> gridnMap = new HashMap<>();//经纬度信息也是装在经纬度List集合中的每个map集合键值对中
            String lng = address.getLng();
            String lat = address.getLat();
            gridnMap.put("lng",lng);
            gridnMap.put("lat",lat);
            gridnMaps.add(gridnMap);

        }

        map.put("nameMaps",nameMaps);
        map.put("gridnMaps",gridnMaps);

        return map;
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){//实体对象做参数要加这个注解
        PageResult pageResult = addressService.findPage(queryPageBean);
        return pageResult;
    }


    @RequestMapping("/addAddress")
    public Result add(@RequestBody Address address){
        try {
            addressService.add(address);
            return new Result(true, MessageConstant.ADD_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ADDRESS_FAIL);
        }
    }


    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            addressService.deleteById(id);
            return new Result(true,MessageConstant.DELETE_ADDRESS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_ADDRESS_FAIL);
        }
    }
}
