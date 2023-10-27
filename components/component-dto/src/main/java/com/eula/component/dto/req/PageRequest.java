package com.eula.component.dto.req;

import com.eula.component.dto.IDto;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求
 * @author xiankun.geng
 */
@Data
public class PageRequest implements Serializable, IDto {

    private static final long SerialVersionUID = 1L;

    /**
     * 默认页面数目大小
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 默认页面最大数目
     */
    private static final int DEFAULT_MAX_PAGE_SIZE = 500;

    /**
     * 当前页面索引
     */
    private int page;

    /**
     * 页面数目大小
     */
    private int perPage;

    public PageRequest() {
    }

    public int getPage() {
        return Math.max(page, 1);
    }

    public int getPerPage() {
        if (0 >= this.perPage) {
            this.perPage = DEFAULT_PAGE_SIZE;
        }
        if (DEFAULT_MAX_PAGE_SIZE < this.perPage) {
            this.perPage = DEFAULT_MAX_PAGE_SIZE;
        }
        return this.perPage;
    }

}

