package com.atguigu.dao;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Address;
import com.github.pagehelper.Page;

import java.util.List;

public interface AddressDao {
    List<Address> findAllMaps();


    Page findPage(String queryString);

    void add(Address address);

    void deleteById(Integer id);
}
