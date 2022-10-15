package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {

    void add(TravelGroup travelGroup);

    void setTravelGroupAndTravelItem(Map<String, Integer> map);


    Page findPage(@Param("queryString") String queryString22);

    TravelGroup getById(Integer id);

    List<Integer> getTravelItemIdsByTravelGroupId(Integer travelGroupId);

    void edit(TravelGroup travelGroup);

    void delete(Integer travelGroupId);

    List<TravelGroup> findAll();

//这是帮助封装套餐对象的travelGroup属性的方法，即查出套餐查询关联的跟团游信息的方法，这里传过来的是套餐的id
    List<TravelGroup> findTravelGroupById(Integer id);
}
