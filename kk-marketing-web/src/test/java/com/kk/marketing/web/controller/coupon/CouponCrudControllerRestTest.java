package com.kk.marketing.web.controller.coupon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kk.arch.common.util.JsonUtils;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.arch.common.vo.PageRespVo;
import com.kk.arch.common.vo.ResponseData;
import com.kk.marketing.coupon.vo.CouponVo;
import com.kk.marketing.web.req.CouponQueryReqVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.kk.arch.common.constants.CommonConstants.HEADER_TOKEN;
import static com.kk.arch.common.vo.ResponseData.SUCCESS;
import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class CouponCrudControllerRestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    private static final Long TEST_TENANT_ID_1 = 9901L;
    private static final Long TEST_TENANT_ID_2 = 9902L;

    @Test
    public void queryPageTest() throws Exception {
        int numberOfThreads = 1000;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            int finalI = i;
            Future<Boolean> future = executorService.submit(() -> {
                try {
                    return this.queryPageTestByTenantId(finalI % 2 == 0 ? "aaa" : "bbb", finalI % 2 == 0 ? TEST_TENANT_ID_1 : TEST_TENANT_ID_2);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                } catch (AssertionFailedError e) {
                    log.error("Assert ERROR: ", e);
                    assertNull(e);
                } catch (Exception e) {
                    log.error("ERROR: ", e);
                    throw e;
                } finally {
                    latch.countDown();
                }
                return false;
            });
            log.info("i: {}", finalI);
            futures.add(future);
            sleep(10);
        }

        // 等待所有任务完成并验证结果
        for (Future<Boolean> future : futures) {
            Boolean response = future.get();
            assertEquals(true, response);
        }

        // 等待所有线程完成
        latch.await();

        // 关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(6000, TimeUnit.SECONDS);
    }

    protected Boolean queryPageTestByTenantId(String token, Long shouldBeTokenId) throws JsonProcessingException {
        HttpHeaders headers1 = new HttpHeaders();
        headers1.set("Content-Type", "application/json");
        headers1.set(HEADER_TOKEN, token);

        PageReqVo<CouponQueryReqVo> pageReqVo = PageReqVo.<CouponQueryReqVo>builder().pageNum(1).pageSize(2).param(CouponQueryReqVo.builder().build()).build();
        String jsonContent = objectMapper.writeValueAsString(pageReqVo);

        HttpEntity<String> entity1 = new HttpEntity<>(jsonContent, headers1);

        ResponseEntity<ResponseData> response = restTemplate.exchange(
                "http://localhost:" + port + "/marketing/web/coupon/queryPage",
                HttpMethod.POST,
                entity1,
                ResponseData.class
        );
        assertEquals(200, response.getStatusCodeValue());
        ResponseData responseData = response.getBody();
        assertEquals(SUCCESS, responseData.getCode());
        assertEquals("", responseData.getMsg());

        final PageRespVo<CouponVo> pageRespVo = JsonUtils.mapToObject((Map<String, Object>) responseData.getData(), PageRespVo.class);
        pageRespVo.setList(JsonUtils.toList(pageRespVo.getList(), CouponVo.class));

        assertEquals(1, pageRespVo.getPageNum());
        assertEquals(2, pageRespVo.getPageSize());

        assertEquals(shouldBeTokenId, pageRespVo.getList().get(0).getTenantId());

        return true;
    }

}
