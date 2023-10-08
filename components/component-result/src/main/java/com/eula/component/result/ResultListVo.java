package com.eula.component.result;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 针对 List 数据类型的返回格式
 * @author xiankun.geng
 */
@Data
@NoArgsConstructor
public class ResultListVo<T> {

    private T list;

    private ResultPagination _meta;

    public ResultListVo(T list) {
        this.setList(list);
    }

    public ResultListVo(T list, ResultPagination _meta) {
        this.setList(list);
        this.set_meta(_meta);
    }

}
