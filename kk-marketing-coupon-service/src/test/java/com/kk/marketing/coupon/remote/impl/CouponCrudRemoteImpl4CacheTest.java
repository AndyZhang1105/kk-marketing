package com.kk.marketing.coupon.remote.impl;

import com.kk.arch.common.vo.ResponseData;
import com.kk.marketing.coupon.conf.TenantContextHolder;
import com.kk.marketing.coupon.remote.CouponCrudRemote;
import com.kk.marketing.coupon.req.CouponQueryReqDto;
import com.kk.marketing.coupon.vo.CouponVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static com.kk.arch.common.constants.CommonConstants.TENANT_ID;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest()
@ActiveProfiles("dev")
@Slf4j
public class CouponCrudRemoteImpl4CacheTest {

    @Autowired
    private CouponCrudRemote couponCrudRemote;

    private static final Long TEST_TENANT_ID = 9999L;

    @BeforeAll
    public static void beforeAll() {
        RpcContext.getContext().setAttachment(TENANT_ID, TEST_TENANT_ID);
        TenantContextHolder.setTenantId(TEST_TENANT_ID);
    }

    @Test
    void listCouponTest() {
        CouponQueryReqDto reqDto = CouponQueryReqDto.builder().build();

        ResponseData<List<CouponVo>> responseData = null;
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            responseData = couponCrudRemote.listCoupon(reqDto);
            Assert.isTrue(responseData.getCode() == ResponseData.SUCCESS, "返回状态值不正确");
            Assert.isTrue(!responseData.getData().isEmpty(), "返回数据不能为空");
            Assert.isTrue(Objects.equals(responseData.getData().get(0).getTenantId(), TEST_TENANT_ID), "返回数据不对");
            long elapsedTime = System.currentTimeMillis() - start;
            log.info("elapsedTime: {}", elapsedTime);
        }
    }

    @Test
    public void listCouponFromMultiThreadTest() throws Exception {
        int numberOfThreads = 100;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<ResponseData<List<CouponVo>>>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            Future<ResponseData<List<CouponVo>>> future = executorService.submit(() -> {
                try {
                    CouponQueryReqDto reqDto = CouponQueryReqDto.builder().build();
                    return couponCrudRemote.listCoupon(reqDto);
                } catch (AssertionFailedError e) {
                    log.error("Assert ERROR: ", e);
                    assertNull(e);
                } catch (Exception e) {
                    log.error("ERROR: ", e);
                    throw e;
                } finally {
                    latch.countDown();
                }
                return null;
            });
            log.info("i: {}", i);
            futures.add(future);
            // sleep(10);
        }

        // 等待所有任务完成并验证结果
        for (Future<ResponseData<List<CouponVo>>> future : futures) {
            ResponseData<List<CouponVo>> responseData = future.get();
            Assert.isTrue(responseData.getCode() == ResponseData.SUCCESS, "返回状态值不正确");
            Assert.isTrue(!responseData.getData().isEmpty(), "返回数据不能为空");
            Assert.isTrue(Objects.equals(responseData.getData().get(0).getTenantId(), TEST_TENANT_ID), "返回数据不对");
        }

        // 等待所有线程完成
        latch.await();

        // 关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(6000, TimeUnit.SECONDS);
    }

}
