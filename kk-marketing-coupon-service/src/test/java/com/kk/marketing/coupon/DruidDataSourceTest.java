package com.kk.marketing.coupon;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import javax.sql.DataSource;

@SpringBootTest()
@ActiveProfiles("dev")
public class DruidDataSourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoad() {
        System.out.println(dataSource.getClass());
        DruidDataSource druidDataSource = (DruidDataSource) dataSource;
        System.out.println("初始化连接数：" + druidDataSource.getInitialSize());
        System.out.println("最大连接数：" + druidDataSource.getMaxActive());
        Assert.isTrue(druidDataSource.getInitialSize() == 5, "初始化连接数应该是5");
        Assert.isTrue(druidDataSource.getMaxActive() == 20, "最大连接数应该是20");
    }

}
