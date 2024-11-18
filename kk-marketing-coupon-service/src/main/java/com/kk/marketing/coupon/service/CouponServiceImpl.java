package com.kk.marketing.coupon.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.marketing.coupon.entity.Coupon;
import com.kk.marketing.coupon.mapper.CouponMapper;
import com.kk.marketing.coupon.req.CouponQueryReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author Zal
 */
@Service
public class CouponServiceImpl extends BaseServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public Map<Long, Coupon> queryMap(Long tenantId, List<Long> idList) {
        // return this.queryList(tenantId, idList).stream().collect(Collectors.toMap(Coupon::getId, Function.identity(), (f, s) -> f));
        return this.queryMap(tenantId, idList, Coupon::getId);
    }

    @Override
    public List<Coupon> queryList(CouponQueryReqDto reqDto) {
        return this.couponMapper.queryList(reqDto);
    }

    @Override
    public PageRespVo<Coupon> queryPage(PageReqVo<CouponQueryReqDto> pageReqVo) {
        Page<Coupon> page = new Page<>(pageReqVo.getPageNum(), pageReqVo.getPageSize());
        List<Coupon> resultList = this.baseMapper.queryPage(page, pageReqVo.getParam());
        return new PageRespVo<>(page.getTotal(), page.getCurrent(), page.getSize(), resultList);
    }

}
