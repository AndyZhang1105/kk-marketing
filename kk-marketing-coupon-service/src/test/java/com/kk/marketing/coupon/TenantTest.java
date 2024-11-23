package com.kk.marketing.coupon;

import com.kk.arch.common.util.CollectionUtils;
import com.kk.marketing.coupon.conf.TenantContextHolder;
import com.kk.marketing.coupon.entity.CouponVerifier;
import com.kk.marketing.coupon.mapper.CouponVerifierMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * <p>
 * 多租户 Tenant 演示
 * </p>
 *
 * @author hubin
 * @since 2018-08-11
 */
@SpringBootTest
@ActiveProfiles("dev")
public class TenantTest {

    @Autowired
    private CouponVerifierMapper mapper;

    private static final Long TEST_TENANT_ID = 9999L;

    @BeforeAll
    public static void beforeAll() {
        TenantContextHolder.setTenantId(TEST_TENANT_ID);
    }

    @AfterAll
    public static void AfterAll() {
        TenantContextHolder.clear();
    }

    @Test
    public void aInsert() {
        CouponVerifier couponVerifier = new CouponVerifier();
        couponVerifier.setVerifierName("ZZ");
        couponVerifier.setVerifierPhone("18601010202");
        Assertions.assertTrue(mapper.insert(couponVerifier) > 0);
        couponVerifier = mapper.selectById(couponVerifier.getId());
        Assertions.assertEquals(TEST_TENANT_ID, (long) couponVerifier.getTenantId());
    }

    @Test
    public void bDelete() {
        List<CouponVerifier> resultList = mapper.selectList(null);
        if(CollectionUtils.isNotEmpty(resultList)) {
            Assertions.assertEquals(1, mapper.deleteById(resultList.get(0).getId()));
        }
    }

    @Test
    public void cUpdate() {
        List<CouponVerifier> resultList = mapper.selectList(null);
        if(CollectionUtils.isNotEmpty(resultList)) {
            Assertions.assertTrue(mapper.updateById(CouponVerifier.builder().id(resultList.get(0).getId()).verifierPhone("18602020303").build()) > 0);
        }
    }

    @Test
    public void dSelect() {
        List<CouponVerifier> resultList = mapper.selectList(null);
        resultList.forEach(u -> Assertions.assertEquals(TEST_TENANT_ID, (long) u.getTenantId()));
    }

    /**
     * 自定义SQL：默认也会增加多租户条件
     * 参考打印的SQL
     */
    @Test
    public void manualSqlTenantFilterTest() {
        System.out.println(mapper.queryList("ZZ"));
    }

}
