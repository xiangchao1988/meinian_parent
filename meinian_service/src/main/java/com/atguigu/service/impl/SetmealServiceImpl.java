package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service(interfaceClass = SetmealService.class)//发布服务，注册到zk上了
@Transactional//声明式事务，所有方法都增加上事务
public class SetmealServiceImpl implements SetmealService{
    @Autowired
    SetmealDao setmealDao;


    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {

        setmealDao.add(setmeal);//主键回填,就是在dao.xml的add配置上

        Integer setmealId = setmeal.getId();
        setSetmealAndTravelGroup(travelgroupIds,setmealId);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page page=setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public Setmeal getSetmealById(Integer id) {
        return setmealDao.getSetmealById(id);
    }

    @Override
    public List<Map> getSetmealReport() {
        return setmealDao.getSetmealReport();
    }


    private void setSetmealAndTravelGroup(Integer[] travelgroupIds, Integer setmealId) {
        if(travelgroupIds !=null && travelgroupIds.length>0){
            for (Integer travelgroupId : travelgroupIds) {
                Map<String,Integer> map=new HashMap();
                map.put("travelgroupId",travelgroupId);
                map.put("setmealId",setmealId);
                setmealDao.addSetmealAndTravelGroup(map);
            }

        }

    }
}
