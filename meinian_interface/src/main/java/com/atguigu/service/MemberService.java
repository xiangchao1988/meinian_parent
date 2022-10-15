package com.atguigu.service;

import java.util.List;

public interface MemberService {
    List<Integer> findMemberCountByMonth(List<String> months);
}
