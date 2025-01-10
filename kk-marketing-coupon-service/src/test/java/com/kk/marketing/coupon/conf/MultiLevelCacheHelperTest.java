package com.kk.marketing.coupon.conf;

import com.arch.dubbo.common.conf.MultiLevelCacheHelper;
import com.arch.dubbo.common.conf.TenantContextHolder;
import com.kk.arch.dubbo.remote.vo.PageReqVo;
import com.kk.arch.dubbo.remote.vo.PageRespVo;
import com.kk.arch.dubbo.remote.vo.ResponseData;
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

import static com.arch.dubbo.common.constant.CommonConstants.TENANT_ID;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest()
@ActiveProfiles("dev")
@Slf4j
public class MultiLevelCacheHelperTest {

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
        PageReqVo<CouponQueryReqDto> pageReqVo = PageReqVo.of(1, 2, CouponQueryReqDto.builder().build());

        for (int i = 0; i < 1000; i++) {
            long start = System.currentTimeMillis();
            final String key = MultiLevelCacheHelper.generateKey(pageReqVo);
            ResponseData<PageRespVo<CouponVo>> responseData = (ResponseData<PageRespVo<CouponVo>>) MultiLevelCacheHelper.get(key, 100, () -> couponCrudRemote.queryPage(pageReqVo));
            assert responseData != null;
            Assert.isTrue(responseData.getCode() == ResponseData.SUCCESS, "返回状态值不正确");
            Assert.isTrue(responseData.getData() != null, "返回数据不能为空");
            Assert.isTrue(Objects.equals(responseData.getData().getList().get(0).getTenantId(), TEST_TENANT_ID), "返回数据不对");
            long elapsedTime = System.currentTimeMillis() - start;
            log.info("elapsedTime: {}", elapsedTime);
        }
    }

    @Test
    public void listCouponFromMultiThreadTest() throws Exception {
        int numberOfThreads = 100;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<ResponseData<PageRespVo<CouponVo>>>> futures = new ArrayList<>();

        PageReqVo<CouponQueryReqDto> pageReqVo = PageReqVo.of(1, 2, CouponQueryReqDto.builder().build());
        final String key = MultiLevelCacheHelper.generateKey(pageReqVo);

        for (int i = 0; i < numberOfThreads; i++) {
            Future<ResponseData<PageRespVo<CouponVo>>> future = executorService.submit(() -> {
                try {
                    return (ResponseData<PageRespVo<CouponVo>>) MultiLevelCacheHelper.get(key, 10, () -> couponCrudRemote.queryPage(pageReqVo));
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
        }

        // 等待所有任务完成并验证结果
        for (Future<ResponseData<PageRespVo<CouponVo>>> future : futures) {
            ResponseData<PageRespVo<CouponVo>> responseData = future.get();
            Assert.isTrue(responseData.getCode() == ResponseData.SUCCESS, "返回状态值不正确");
            Assert.isTrue(responseData.getData() != null, "返回数据不能为空");
            Assert.isTrue(Objects.equals(responseData.getData().getList().get(0).getTenantId(), TEST_TENANT_ID), "返回数据不对");
        }

        // 等待所有线程完成
        latch.await();

        // 关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(6000, TimeUnit.SECONDS);
    }

}
