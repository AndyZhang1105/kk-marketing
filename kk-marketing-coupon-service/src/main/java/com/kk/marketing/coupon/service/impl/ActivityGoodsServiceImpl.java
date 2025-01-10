package com.kk.marketing.coupon.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.kk.marketing.coupon.entity.ActivityGoods;
import com.kk.marketing.coupon.mapper.ActivityGoodsMapper;
import com.kk.marketing.coupon.service.ActivityGoodsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动相关的商品信息 服务实现类
 * </p>
 *
 * @author Zal
 * @since 2024-11-19
 */
@Service
public class ActivityGoodsServiceImpl extends BaseServiceImpl<ActivityGoodsMapper, ActivityGoods> implements ActivityGoodsService {

    @Override
    public List<ActivityGoods> queryList(Integer activityType, List<Long> activityIdList) {
        return new LambdaQueryChainWrapper<>(this.getBaseMapper())
                .eq(ActivityGoods::getActivityType, activityType)
                .in(ActivityGoods::getActivityId, activityIdList)
                .list();
    }

    @Override
    public Map<Long, List<ActivityGoods>> queryGroupMap(Integer activityType, List<Long> activityIdList) {
        final List<ActivityGoods> activityStoreList = this.queryList(activityType, activityIdList);
        return activityStoreList.stream().collect(Collectors.groupingBy(ActivityGoods::getActivityId));
    }

}
