package cn.wangzhen.service;

import cn.wangzhen.domain.CheckItem;
import cn.wangzhen.entity.PageResult;
import cn.wangzhen.entity.QueryPageBean;
import cn.wangzhen.entity.Result;

import java.util.List;

//服务接口
public interface CheckItemService {

    void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void delect(int id);

    CheckItem findById(int id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

}
