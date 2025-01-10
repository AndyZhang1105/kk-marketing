package com.kk.marketing.coupon.service;

import com.kk.marketing.coupon.entity.ActivityStore;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 适用门店信息表 服务类
 * </p>
 *
 * @author Zal
 * @since 2024-12-06
 */
public interface ActivityStoreService extends BaseService<ActivityStore> {

    List<ActivityStore> queryList(Integer activityType, List<Long> activityIdList);

    Map<Long, List<ActivityStore>> queryGroupMap(Integer activityType, List<Long> activityIdList);

}
