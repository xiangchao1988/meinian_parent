package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelItem")//对应实体类的名字，首字母小写
public class TravelItemController {

    @Reference // 远程调用服务
    TravelItemService travelItemService;

    @RequestMapping("/findAll")
    public Result findAll(){
        List<TravelItem> list = travelItemService.findAll();
        return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,list);
    }

    @RequestMapping("/eidt")
    public Result eidt(@RequestBody TravelItem travelItem){//传的是对象要加这个注解，才可以从请求体获取数据
        try {
            travelItemService.edit(travelItem);
            return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.EDIT_TRAVELITEM_FAIL);
    }
    }

    @RequestMapping("/getById")
    public Result getById(Integer id){
        try {
            TravelItem travelItem= travelItemService.getById(id);
            return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            travelItemService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

            PageResult pageResult=  travelItemService.findPage(queryPageBean.getCurrentPage(),
                    queryPageBean.getPageSize(),queryPageBean.getQueryString());//ctrl+alt+t
            return pageResult;

    }

    @RequestMapping("/add")
    public Result add(@RequestBody TravelItem travelItem){//表单中所有的字段名称都要和实体对象的属性名一致，并提供响应的set方法，才能被springmvc框架封装为bean对象
                        //@RequestBody这个注解代表从请求体中获取数据进行封装，页面的确定按钮绑定的事件是通过axios的post请求提交表单
        try {
            travelItemService.add(travelItem);//ctrl+alt+t
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_TRAVELITEM_FAIL);
        }

    }


}
