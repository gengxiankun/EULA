package com.eula.component.curd.easy.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eula.component.dto.IDto;
import com.eula.component.dto.req.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * EasyService 解决 BaseService 实现时依赖较多请求实体带来影响开发效率的情况，并且动态识别查询时是否带分页的情况。
 * @param <M> BaseMapper
 * @param <T> Entity
 * @param <Req> 请求实体
 * @param <Resp> 响应实体
 * @author gengxiankun
 */
public abstract class EasyService<M extends BaseMapper<T>, T, Req extends IDto, Resp extends IDto> extends ServiceImpl<M, T> {

    protected final Class<?>[] typeArguments = GenericTypeUtils.resolveTypeArguments(this.getClass(), EasyService.class);

    protected Class<Resp> respClass = this.getRespClass();

    public IPage<Resp> list(PageRequest req) {
        Wrapper<T> wrapper = this.getWrapper((Req) req);
        Page<T> page = new Page<>(req.getPage(), req.getPerPage());
        Page<T> result = this.page(page, wrapper);
        return result.convert(this::getResp);
    }

    public List<Resp> list(Req req) {
        Wrapper<T> wrapper = this.getWrapper(req);
        List<T> result = this.list(wrapper);
        return result.stream().map(this::getResp).collect(Collectors.toList());
    }

    public Resp findById(Long id) {
        T entity = this.getBaseMapper().selectById(id);
        return this.getResp(entity);
    }

    public T save(Req req) {
        T entity = this.convertByReq(req);
        this.save(entity);
        return entity;
    }

    public void update(Req req) {
        this.updateById(this.convertByReq(req));
    }

    public void delete(Long id) {
        this.removeById(id);
    }

    @SuppressWarnings("unchecked")
    protected Class<Resp> getRespClass() {
        return (Class<Resp>) this.typeArguments[5];
    }

    protected Resp getResp(T entity) {
        return BeanUtil.copyProperties(entity, this.respClass);
    }

    protected abstract Wrapper<T> getWrapper(Req req);

    protected T convertByReq(Req req) {
        return BeanUtil.copyProperties(req, this.entityClass);
    }

}
