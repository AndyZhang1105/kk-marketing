package com.kk.marketing.coupon.service.impl;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.arch.common.util.AssertUtils;
import com.kk.arch.common.util.JsonUtils;
import com.kk.arch.common.util.StringUtils;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.marketing.coupon.service.BaseService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.kk.arch.common.constants.CommonConstants.NO;
import static com.kk.arch.common.constants.CommonConstants.YES;


/**
 * @author Zal
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    public final String FIELD_TENANT_ID = "tenant_id";
    public final String FIELD_ID = "id";
    public final String FIELD_ACTIVE_STATUS = "active_status";
    public final String FIELD_DELETED = "deleted";
    public final String FIELD_UPDATE_BY = "update_by";

    @Override
    public Boolean addOne(Object param) {
        T coupon = JsonUtils.toObject(param, this.getEntityClass());
        return this.save(coupon);
    }

    @Override
    public Boolean activate(Long tenantId, Long id) {
        final T one = this.queryOne(tenantId, id);
        AssertUtils.isNotNull(one, "要启用的目标不存在");
        return new UpdateChainWrapper<T>(this.baseMapper)
                .eq(FIELD_TENANT_ID, tenantId)
                .eq(FIELD_ID, id)
                .set(FIELD_ACTIVE_STATUS, YES)
                .update();
    }

    @Override
    public Boolean deactivate(Long tenantId, Long id) {
        final T one = this.queryOne(tenantId, id);
        AssertUtils.isNotNull(one, "要禁用的目标不存在");
        return new UpdateChainWrapper<T>(this.baseMapper)
                .eq(FIELD_TENANT_ID, tenantId)
                .eq(FIELD_ID, id)
                .set(FIELD_ACTIVE_STATUS, NO)
                .update();
    }

    @Override
    public boolean updateOrSave(T param) {
        final Map<String, Object> fieldMap = JsonUtils.toMap(param);
        return new UpdateChainWrapper<>(this.baseMapper)
                .eq(FIELD_TENANT_ID, fieldMap.get(StringUtils.toCamelCase(FIELD_TENANT_ID)))
                .eq(FIELD_ID, fieldMap.get(StringUtils.toCamelCase(FIELD_ID)))
                .update() || this.save(param);
    }

    @Override
    public boolean increaseOrSave(T param, String fieldName, int num) {
        final Map<String, Object> fieldMap = JsonUtils.toMap(param);
        fieldMap.put(fieldName, num);
        return new UpdateChainWrapper<>(this.baseMapper)
                .allEq(JsonUtils.toMap(param))
                .setIncrBy(fieldName, num)
                .update() || this.save(JsonUtils.mapToObject(fieldMap, getEntityClass()));
    }

    @Override
    public boolean delete(Long tenantId, Long id, Long operatorId) {
        return new UpdateChainWrapper<T>(this.baseMapper)
                .eq(FIELD_TENANT_ID, tenantId)
                .eq(FIELD_ID, id)
                .set(FIELD_DELETED, YES)
                .set(FIELD_UPDATE_BY, operatorId)
                .update();
    }

    @Override
    public boolean delete(Long tenantId, List<Long> idList, Long operatorId) {
        return new UpdateChainWrapper<T>(this.baseMapper)
                .eq(FIELD_TENANT_ID, tenantId)
                .in(FIELD_ID, idList)
                .set(FIELD_DELETED, YES)
                .set(FIELD_UPDATE_BY, operatorId)
                .update();
    }

    @Override
    public T queryOne(Long tenantId, Long id) {
        final Map<String, Object> whereMap = Maps.newHashMap();
        whereMap.put(FIELD_TENANT_ID, tenantId);
        whereMap.put(FIELD_ID, id);
        final List<T> resultList = this.baseMapper.selectByMap(whereMap);
        return Optional.ofNullable(resultList).orElse(Collections.emptyList()).stream().findFirst().orElse(null);
    }

    @Override
    public T queryOne(T object) {
        return this.queryList(object).stream().findFirst().orElse(null);
    }

    @Override
    public List<T> queryList(T object) {
        final Map<String, Object> whereMap = JsonUtils.toMap(object);
        return this.baseMapper.selectByMap(whereMap);
    }

    @Override
    public List<T> queryList(Long tenantId, List<Long> idList) {
        return new QueryChainWrapper<>(this.baseMapper)
                .eq(FIELD_TENANT_ID, tenantId)
                .in(FIELD_ID, idList)
                .list();
    }

    @Override
    public Map<Long, T> queryMap(Long tenantId, List<Long> idList, Function<T, Long> keyMapper) {
        return queryList(tenantId, idList).stream().collect(Collectors.toMap(keyMapper, Function.identity(), (f, s) -> f));
    }

    @Override
    public PageRespVo<T> queryPageByEntity(PageReqVo<T> pageReqVo) {
        Page<T> page = new Page<>(pageReqVo.getPageNum(), pageReqVo.getPageSize());
        final T param = JsonUtils.toObject(pageReqVo.getParam(), this.getEntityClass());
        QueryWrapper<T> queryWrapper = new QueryWrapper<>(param);
        Page<T> resultPage = this.baseMapper.selectPage(page, queryWrapper);
        return new PageRespVo<>(page.getTotal(), page.getCurrent(), page.getSize(), resultPage.getRecords());
    }

}
