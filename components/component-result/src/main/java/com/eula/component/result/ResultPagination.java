package com.eula.component.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * 数据分页实体类
 * @author xiankun.geng
 */
@Data
public class ResultPagination {

    /**
     * 总条目
     */
    private long totalCount;

    /**
     * 总页数
     */
    private long pageCount;

    /**
     * 当前页
     */
    private long currentPage;

    /**
     * 每页条目数
     */
    private long perPage;

    public ResultPagination() {
    }

    public ResultPagination(Page<?> page) {
        this.totalCount = page.getTotalElements();
        this.pageCount = page.getTotalPages();
        // 修正 JPA 分页默认从 0 开始的问题
        this.currentPage = page.getNumber() + 1L;
        this.perPage = page.getSize();
    }

    public ResultPagination(IPage<?> page) {
        this.totalCount = page.getTotal();
        this.currentPage = page.getCurrent();
        this.perPage = page.getSize();
        this.pageCount = page.getPages();
    }

}
