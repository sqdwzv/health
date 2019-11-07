package cn.wangzhen.service;

import cn.wangzhen.entity.Result;

import java.util.Map;

public interface OrderService {
    Result order(Map map) throws Exception;


    Map findById(int id) throws Exception;
}
