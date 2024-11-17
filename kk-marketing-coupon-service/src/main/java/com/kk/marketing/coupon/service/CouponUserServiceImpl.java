package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.arch.common.util.JsonUtils;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.marketing.coupon.entity.CouponUser;
import com.kk.marketing.coupon.mapper.CouponUserMapper;
import com.kk.marketing.coupon.vo.CouponUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Zal
 */
@Service
public class CouponUserServiceImpl extends ServiceImpl<CouponUserMapper, CouponUser> implements CouponUserService {

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

}
