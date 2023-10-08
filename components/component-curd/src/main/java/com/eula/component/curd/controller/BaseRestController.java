package com.eula.component.curd.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eula.component.dto.IDto;
import com.eula.component.dto.req.PageRequest;
import com.eula.component.curd.service.BaseService;
import org.springframework.web.bind.annotation.*;

/**
 * BaseRestController，按照 Restful 风格实现了通过的 CURD 接口。
 * @author gengxiankun
 * @param <S> BaseService
 * @param <M> BaseMapper
 * @param <T> Entity
 * @param <SearchReq> 查询请求实体
 * @param <SaveReq> 保存请求实体
 * @param <UpdateReq> 更新请求实体
 * @param <Resp> 响应实体
 */
public abstract class BaseRestController<S extends BaseService<M , T, SearchReq, SaveReq, UpdateReq, Resp>, M extends BaseMapper<T>, T, SearchReq extends PageRequest, SaveReq extends IDto, UpdateReq extends IDto, Resp extends IDto> {

    protected final S baseService;

    public BaseRestController(S baseService) {
        this.baseService = baseService;
    }

    /**
     * 获取列表
     * @param req 查询参数
     * @return 列表数据
     */
    @GetMapping
    public IPage<Resp> search(SearchReq req) {
        return this.baseService.search(req);
    }

    /**
     * 获取详情
     * @param id ID
     * @return 详情数据
     */
    @GetMapping("/{id}")
    public Resp findById(@PathVariable Long id) {
        return this.baseService.findById(id);
    }

    /**
     * 保存
     * @param req 保存信息
     */
    @PostMapping
    public void save(@RequestBody SaveReq req) {
        this.baseService.save(req);
    }

    /**
     * 更新
     * @param req 更新信息
     */
    @PutMapping
    public void update(@RequestBody UpdateReq req) {
        this.baseService.update(req);
    }

    /**
     * 删除
     * @param id ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.baseService.delete(id);
    }

}
