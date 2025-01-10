package com.kk.marketing.coupon.service;

import com.kk.marketing.coupon.entity.ActivityGoods;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动相关的商品信息 服务类
 * </p>
 *
 * @author Zal
 * @since 2024-11-19
 */
public interface ActivityGoodsService extends BaseService<ActivityGoods> {

    List<ActivityGoods> queryList(Integer activityType, List<Long> activityIdList);

    Map<Long, List<ActivityGoods>> queryGroupMap(Integer activityType, List<Long> activityIdList);
}
