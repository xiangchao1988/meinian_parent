package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.AddressDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass=AddressService.class)//发布服务，注册到zk上了
@Transactional//声明式事务，所有方法都增加上事务
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressDao addressDao;

    @Override
    public List<Address> findAllMaps() {
        return addressDao.findAllMaps();
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //第一步：利用分页插件开启分页，传当前第几页，每页数据行数过去
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //第二步：调dao接口方法传搜索框输入的条件去查，获得分页插件中的page分页对象
        Page page = addressDao.findPage(queryPageBean.getQueryString());
        //第三步：因为要返回一个PageResult对象，用分页插件page对象的查询总结果数和当前结果数构造一个PageResult对象返回
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());

        return pageResult;
    }

    @Override
    public void add(Address address) {
        addressDao.add(address);
    }

    @Override
    public void deleteById(Integer id) {
        addressDao.deleteById(id);
    }
}
