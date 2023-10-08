package com.eula.component.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.reflect.GenericTypeUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eula.component.dto.IDto;
import com.eula.component.dto.req.PageRequest;

/**
 * Service 抽象类，通常在分层架构中存在频繁的类型转换操作（不能循环依赖），继承此类并实现 `component-dto` 中的 `IDto` 接口能够自动进行类型转换。
 * 此类还继承了 `Mybatis-Plus` 中的 `ServiceImpl` 接口，实现了通用的 CURD 操作。
 * @param <M> BaseMapper
 * @param <T> Entity
 * @param <SearchReq> 查询请求实体
 * @param <SaveReq> 保存请求实体
 * @param <UpdateReq> 更新请求实体
 * @param <Resp> 响应实体
 * @author gengxiankun
 */
public abstract class AbstractService<M extends BaseMapper<T>, T, SearchReq extends PageRequest, SaveReq extends IDto, UpdateReq extends IDto, Resp extends IDto> extends ServiceImpl<M, T> {

    protected final Class<?>[] typeArguments = GenericTypeUtils.resolveTypeArguments(this.getClass(), AbstractService.class);

    protected Class<Resp> respClass = this.getRespClass();

    public IPage<Resp> search(SearchReq req) {
        Wrapper<T> wrapper = this.getWrapper(req);
        Page<T> page = new Page<>(req.getPage(), req.getPerPage());
        Page<T> result = this.page(page, wrapper);
        return result.convert(this::getResp);
    }

    public Resp findById(Long id) {
        T entity = this.getBaseMapper().selectById(id);
        return this.getResp(entity);
    }
    
    public void save(SaveReq req) {
        this.save(this.convertBySaveReq(req));
    }
    
    public void update(UpdateReq req) {
        this.updateById(this.convertByUpdateReq(req));
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

    protected abstract Wrapper<T> getWrapper(SearchReq req);

    protected T convertBySaveReq(SaveReq req) {
        return BeanUtil.copyProperties(req, this.entityClass);
    }

    protected T convertByUpdateReq(UpdateReq req) {
        return BeanUtil.copyProperties(req, this.entityClass);
    }

}
