package com.kk.marketing.coupon.adapter;

import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Zal
 */
@Component
public class ProductQueryAdapter {

//    @DubboReference
//    private ProductQueryService productQueryService;

    public Map<String, List<String>> queryCategoryGroupMap(List<String> upcList) {
        // todo
        return Maps.newHashMap();
    }

    public Map<String, List<String>> queryBrandGroupMap(List<String> upcList) {
        // todo
        return Maps.newHashMap();
    }

}
