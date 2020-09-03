package com.ktz.sh.base.page;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName Page
 * @Description 自定义分页对象
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 10:28
 * @Version V1.0.0
 **/
public class Page<T> implements Serializable {
    private List<T> list;
    private long total;
    private int pageNum;
    private int pageSize;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}