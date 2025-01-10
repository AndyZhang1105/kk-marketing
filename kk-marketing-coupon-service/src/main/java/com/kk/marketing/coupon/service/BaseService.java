package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kk.arch.dubbo.remote.vo.PageReqVo;
import com.kk.arch.dubbo.remote.vo.PageRespVo;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Zal
 */
public interface BaseService<T> extends IService<T> {

    Boolean addOne(Object param);

    Boolean activate(Long id);

    Boolean deactivate(Long id);

    boolean updateOrSave(T param);

    boolean increaseOrSave(T param, String fieldName, int num);

    boolean delete(Long id, Long operatorId);

    boolean delete(List<Long> idList, Long operatorId);

    T queryOne(Long id);

    T queryOne(T object);

    List<T> queryList(T object);

    List<T> queryList(List<Long> idList);

    Map<Long, T> queryMap(List<Long> idList, Function<T, Long> keyMapper);

    PageRespVo<T> queryPageByEntity(PageReqVo<T> pageReqVo);
}
