package com.kk.marketing.coupon.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kk.marketing.coupon.entity.ActivityStore;
import com.kk.marketing.coupon.mapper.ActivityStoreMapper;
import com.kk.marketing.coupon.service.ActivityStoreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 适用门店信息表 服务实现类
 * </p>
 *
 * @author Zal
 * @since 2024-12-06
 */
@Service
public class ActivityStoreServiceImpl extends BaseServiceImpl<ActivityStoreMapper, ActivityStore> implements ActivityStoreService {

    @Override
    public List<ActivityStore> queryList(Integer activityType, List<Long> activityIdList) {
        return new LambdaQueryChainWrapper<>(this.getBaseMapper())
                .eq(ActivityStore::getActivityType, activityType)
                .in(ActivityStore::getActivityId, activityIdList)
                .list();
    }

    @Override
    public Map<Long, List<ActivityStore>> queryGroupMap(Integer activityType, List<Long> activityIdList) {
        final List<ActivityStore> activityStoreList = this.queryList(activityType, activityIdList);
        return activityStoreList.stream().collect(Collectors.groupingBy(ActivityStore::getActivityId));
    }

}
