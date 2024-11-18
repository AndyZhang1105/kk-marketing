package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Zal
 */
public interface BaseService<T> extends IService<T> {

    Boolean addOne(Object param);

    Boolean activate(Long tenantId, Long id);

    Boolean deactivate(Long tenantId, Long id);

    boolean updateOrSave(T param);

    boolean increaseOrSave(T param, String fieldName, int num);

    boolean delete(Long tenantId, Long id, Long operatorId);

    boolean delete(Long tenantId, List<Long> idList, Long operatorId);

    T queryOne(Long tenantId, Long id);

    T queryOne(T object);

    List<T> queryList(T object);

    List<T> queryList(Long tenantId, List<Long> idList);

    Map<Long, T> queryMap(Long tenantId, List<Long> idList, Function<T, Long> keyMapper);

    PageRespVo<T> queryPageByEntity(PageReqVo<T> pageReqVo);
}
