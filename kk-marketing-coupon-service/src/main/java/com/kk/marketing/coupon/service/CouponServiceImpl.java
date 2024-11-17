package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kk.arch.common.util.AssertUtils;
import com.kk.arch.common.util.JsonUtils;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.mapper.CouponMapper;
import com.kk.marketing.coupon.vo.CouponVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public Boolean addCoupon(CouponVo couponVo) {
        Coupon coupon = JsonUtils.toObject(couponVo, Coupon.class);
        return this.save(coupon);
    }

    @Override
    public Boolean activate(CouponVo couponVo) {
        final Coupon coupon = this.queryOne(couponVo);
        AssertUtils.isNotNull(coupon, "要启用的券不存在");
        return new LambdaUpdateChainWrapper<>(couponMapper)
                .eq(Coupon::getTenantId, couponVo.getTenantId())
                .eq(Coupon::getId, couponVo.getId())
                .set(Coupon::getActiveStatus, YES)
                .update();
    }

    @Override
    public Boolean deactivate(CouponVo couponVo) {
        final Coupon coupon = this.queryOne(couponVo);
        AssertUtils.isNotNull(coupon, "要禁用的券不存在");
        return new LambdaUpdateChainWrapper<>(couponMapper)
                .eq(Coupon::getTenantId, couponVo.getTenantId())
                .eq(Coupon::getId, couponVo.getId())
                .set(Coupon::getActiveStatus, NO)
                .update();
    }

    @Override
    public boolean delete(CouponVo couponVo) {
        return new LambdaUpdateChainWrapper<>(couponMapper)
                .eq(Coupon::getTenantId, couponVo.getTenantId())
                .eq(Coupon::getId, couponVo.getId())
                .set(Coupon::getDeleted, YES)
                .update();
    }

    @Override
    public Coupon queryOne(CouponVo couponVo) {
        final List<Coupon> couponList = this.queryList(couponVo);
        return Optional.ofNullable(couponList).orElse(Collections.emptyList()).stream().findFirst().orElse(null);
    }

    @Override
    public List<Coupon> queryList(CouponVo couponVo) {
        final Coupon coupon = JsonUtils.toObject(couponVo, Coupon.class);
        return couponMapper.queryList(coupon);
    }

    @Override
    public List<Coupon> queryList(Long tenantId, List<Long> idList) {
        return new LambdaQueryChainWrapper<>(couponMapper)
                .eq(Coupon::getTenantId, tenantId)
                .in(Coupon::getId, idList)
                .list();
    }

    @Override
    public Map<Long, Coupon> queryMap(Long tenantId, List<Long> idList) {
        return queryList(tenantId, idList).stream().collect(Collectors.toMap(Coupon::getId, Function.identity(), (f, s) -> f));
    }

    @Override
    public PageRespVo<Coupon> queryPage(PageReqVo<Coupon> pageReqVo) {
        Page<Coupon> page = new Page<>(pageReqVo.getPageNum(), pageReqVo.getPageSize());
        final Coupon coupon = JsonUtils.toObject(pageReqVo.getParam(), Coupon.class);
        List<Coupon> resultList = couponMapper.queryPage(page, coupon);
        return new PageRespVo<>(page.getTotal(), page.getCurrent(), page.getSize(), resultList);
    }

}
