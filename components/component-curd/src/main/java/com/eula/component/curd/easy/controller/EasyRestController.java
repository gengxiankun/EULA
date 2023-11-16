package com.eula.component.curd.easy.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.eula.component.curd.easy.service.EasyService;
import com.eula.component.dto.IDto;
import com.eula.component.dto.req.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class EasyRestController<S extends EasyService<M, T, Req, Resp>, M extends BaseMapper<T>, T, Req extends IDto, Resp extends IDto> {

    protected final S baseService;

    public EasyRestController(S baseService) {
        this.baseService = baseService;
    }

    /**
     * 获取分页数据
     * @param req 查询参数
     * @return 分页数据
     */
    @GetMapping
    public IPage<Resp> page(Req req) {
        return this.baseService.list((PageRequest) req);
    }

    /**
     * 获取列表数据
     * @param req 查询参数
     * @return 列表数据
     */
    @GetMapping(params = "disablePage=true")
    public List<Resp> list(Req req) {
        return this.baseService.list(req);
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
    public void save(@RequestBody Req req) {
        this.baseService.save(req);
    }

    /**
     * 更新
     * @param req 更新信息
     */
    @PutMapping
    public void update(@RequestBody Req req) {
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
