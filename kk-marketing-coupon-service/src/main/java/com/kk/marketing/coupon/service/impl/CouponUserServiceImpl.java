package com.kk.marketing.coupon.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.kk.arch.dubbo.common.util.CollectionUtils;
import com.kk.arch.dubbo.common.util.DateUtils;
import com.kk.arch.dubbo.common.util.JsonUtils;
import com.kk.arch.dubbo.remote.vo.PageReqVo;
import com.kk.arch.dubbo.remote.vo.PageRespVo;
import com.kk.marketing.coupon.entity.CouponUser;
import com.kk.marketing.coupon.enums.CouponUserStatusEnum;
import com.kk.marketing.coupon.mapper.CouponUserMapper;
import com.kk.marketing.coupon.service.CouponUserService;
import com.kk.marketing.coupon.vo.CouponUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Zal
 */
@Service
public class CouponUserServiceImpl extends ServiceImpl<CouponUserMapper, CouponUser> implements CouponUserService {

    @Value("${sql.select.limit:100}")
    private int sqlSelectLimit;

    @Autowired
    private CouponUserMapper couponUserMapper;

    @Override
    public CouponUser queryOne(CouponUserVo couponUserVo) {
        final List<CouponUser> couponList = this.queryList(couponUserVo);
        return Optional.ofNullable(couponList).orElse(Collections.emptyList()).stream().findFirst().orElse(null);
    }

    @Override
    public List<CouponUser> queryList(CouponUserVo couponUserVo) {
        final CouponUser couponUser = JsonUtils.toObject(couponUserVo, CouponUser.class);
        return couponUserMapper.queryList(couponUser);
    }

    @Override
    public List<CouponUser> queryList(Long tenantId, List<Long> idList) {
        return new LambdaQueryChainWrapper<>(couponUserMapper)
                .eq(CouponUser::getTenantId, tenantId)
                .in(CouponUser::getId, idList)
                .list();
    }

    @Override
    public Map<Long, CouponUser> queryMap(Long tenantId, List<Long> idList) {
        return queryList(tenantId, idList).stream().collect(Collectors.toMap(CouponUser::getId, Function.identity(), (f, s) -> f));
    }

    @Override
    public PageRespVo<CouponUser> queryPage(PageReqVo<CouponUser> pageReqVo) {
        Page<CouponUser> page = new Page<>(pageReqVo.getPageNum(), pageReqVo.getPageSize());
        final CouponUser couponUser = JsonUtils.toObject(pageReqVo.getParam(), CouponUser.class);
        List<CouponUser> resultList = couponUserMapper.queryPage(page, couponUser);
        return new PageRespVo<>(page.getTotal(), page.getCurrent(), page.getSize(), resultList);
    }

    @Override
    public List<CouponUser> listCanConsumeCouponUser(Long userId) {
        Date now = DateUtils.getNow();
        return new LambdaQueryChainWrapper<>(couponUserMapper)
                .eq(CouponUser::getUserId, userId)
                .eq(CouponUser::getStatus, CouponUserStatusEnum.UNUSED.getCode())
                .ge(CouponUser::getUsableStartTime, now)
                .le(CouponUser::getUsableEndTime, now)
                .last("limit " + sqlSelectLimit)
                .list();
    }

    @Override
    public Map<String, CouponUser> queryMap(List<String> couponCodeList) {
        if(CollectionUtils.isEmpty(couponCodeList)) {
            return Maps.newHashMap();
        }
        List<CouponUser> couponUserList = new LambdaQueryChainWrapper<>(couponUserMapper)
                .in(CouponUser::getCouponCode, couponCodeList)
                .list();
        return couponUserList.stream().collect(Collectors.toMap(CouponUser::getCouponCode, Function.identity(), (f, s) -> f));
    }

}
