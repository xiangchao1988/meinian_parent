package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.atguigu.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/travelGroup")//对应实体类的名字，首字母小写
public class TravelGroupController {
    @Reference
    TravelGroupService travelGroupService;


    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<TravelGroup> listAll=travelGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,listAll);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/edit")
    //接收两部分数据，分别是根据地址传过来和请求体传过来，根据请求体来的要加@RequestBody注释，自动从请求体获取数据
    public Result edit(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){
        try {
            travelGroupService.edit(travelItemIds,travelGroup);
            return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/getTravelItemIdsByTravelGroupId")
    public Result getTravelItemIdsByTravelGroupId(Integer travelGroupId){
        try {
            List<Integer> travelItemIds = travelGroupService.getTravelItemIdsByTravelGroupId(travelGroupId);
            return new Result(true,"跟团游查询自由行成功",travelItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"跟团游查询自由行成功");
        }

    }

    @RequestMapping("/getById")
    public Result getById(Integer id){

        try {
            TravelGroup travelGroup = travelGroupService.getById(id);
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }


    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=  travelGroupService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/add")
    //接收两部分数据，分别是根据地址传过来和请求体传过来，根据请求体来的要加@RequestBody注释，自动从请求体获取数据
    public Result add(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){
        try {
            travelGroupService.add(travelItemIds,travelGroup);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
    }
}
