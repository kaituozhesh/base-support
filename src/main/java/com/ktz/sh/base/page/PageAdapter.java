package com.ktz.sh.base.page;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @ClassName PageAdapter
 * @Description 分页适配器
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 10:28
 * @Version V1.0.0
 **/
public class PageAdapter<T> {
    public Page<T> fill(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        Page<T> page = new Page<>();
        page.setList(list);
        page.setTotal(pageInfo.getTotal());
        // 增加了返回页码和每页记录数，为了支持app异步查询
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        return page;
    }
}
