package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass =MemberService.class)//发布服务，注册到zk上了
@Transactional//声明式事务，所有方法都增加上事务
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;

    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {

        List list = new ArrayList();//因为返回值是一个集合，所以这里构造一个集合

        if (months!=null && months.size()>0){
            for (String month : months) {
                int count = memberDao.findMemberCountByMonth(month);
                list.add(count);
            }
        }

        return list;
    }
}
