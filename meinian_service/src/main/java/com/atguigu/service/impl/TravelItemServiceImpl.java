package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelItemService.class)//发布服务，注册到zk上了
@Transactional//声明式事务，所有方法都增加上事务
public class TravelItemServiceImpl implements TravelItemService {

    @Autowired
    TravelItemDao travelItemDao;

    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //分页插件PageHelper，开启分页功能startPage
        //底层生成limit ?,?
        //limit(currentPage-1)*(pageSize)，pageSize 比如limit(2-1)*(10) 10 第二页就是从第10个开始查，查10个
        PageHelper.startPage(currentPage,pageSize);

        Page page =travelItemDao.findPage(queryString);//返回当前页的数据
        return new PageResult(page.getTotal(),page.getResult());//1、总记录数 2、当前页结果集合
    }

    @Override
    public void delete(Integer id) {
        //如果存在关联数据，会删除失败，要做个判断，差关联表中是否存在这个自由行id,存在就抛异常，不进行删除
       long count= travelItemDao.findCountByTravelitemId(id);
        if(count>0){
            throw new RuntimeException("删除自由行失败，因为存在关联数据,先解除关系再删除");
        }

        travelItemDao.delete(id);
    }

    @Override
    public TravelItem getById(Integer id) {
       return travelItemDao.getById(id);
    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }
}
