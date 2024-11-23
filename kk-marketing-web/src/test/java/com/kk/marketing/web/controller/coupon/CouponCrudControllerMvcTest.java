package com.kk.marketing.web.controller.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.kk.arch.common.vo.PageReqVo;
import com.kk.marketing.web.req.CouponQueryReqVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.kk.arch.common.constants.CommonConstants.HEADER_TOKEN;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponCrudController.class)
public class CouponCrudControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void queryPageTest() throws Exception {
        PageReqVo<CouponQueryReqVo> pageReqVo = PageReqVo.<CouponQueryReqVo>builder().pageNum(1).pageSize(2).param(CouponQueryReqVo.builder().build()).build();
        String jsonContent = objectMapper.writeValueAsString(pageReqVo);
        final MvcResult result = mockMvc.perform(post("/marketing/web/coupon/queryPage").header(HEADER_TOKEN, "aaa").contentType("application/json").content(jsonContent))
                                      .andExpect(status().isOk()).andReturn();
        String responseContent = result.getResponse().getContentAsString();
        String data = JsonPath.read(responseContent, "$.data");
        assertNull(data);
    }

}
