package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = TravelGroupService.class)//发布服务，注册到zk上了
@Transactional//声明式事务，所有方法都增加上事务
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    TravelGroupDao travelGroupDao;

    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.add(travelGroup);//id是数据写入数据库时数据库分配的，所以这时候还没有id
        Integer travelGroupId = travelGroup.getId();//要进行主键回填才能拿到id,在dao.xml中设置主键回填属性
        setTravelGroupAndTravelItem(travelGroupId,travelItemIds);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page page = travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());//数据封装
    }

    @Override
    public TravelGroup getById(Integer id) {
        TravelGroup travelGroup= travelGroupDao.getById(id);
        return travelGroup;
    }

    @Override
    public List<Integer> getTravelItemIdsByTravelGroupId(Integer travelGroupId) {
        return travelGroupDao.getTravelItemIdsByTravelGroupId(travelGroupId);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {

        travelGroupDao.edit(travelGroup);//id是数据写入数据库时数据库分配的，所以这时候还没有id
        Integer travelGroupId = travelGroup.getId();//要进行主键回填才能拿到id,在dao.xml中设置主键回填属性

        //先删除中间表中该id的数据
        travelGroupDao.delete(travelGroupId);

        //重新再增加中间表的数据
        setTravelGroupAndTravelItem(travelGroupId,travelItemIds);
    }

    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();

    }

    //将数据插入到中间表中
    private void setTravelGroupAndTravelItem(Integer travelGroupId, Integer[] travelItemIds) {
        if (travelItemIds!=null && travelItemIds.length>0){
            for (Integer travelItemId : travelItemIds) {
                // 如果有多个参数使用map
                //利用map集合传递没一组数据，1---1，1---2，1---3类似这种，传三次，一次一次加到数据库中
                Map<String, Integer> map = new HashMap<>();
                map.put("travelGroup",travelGroupId);
                map.put("travelItem",travelItemId);
                travelGroupDao.setTravelGroupAndTravelItem(map);
            }
    }
}
}
