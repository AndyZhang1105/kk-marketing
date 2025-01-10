package com.kk.marketing.coupon.conf;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Zal
 */
@Configuration
@MapperScan(basePackages = {"com.kk.*.*.mapper"})
public class MyBatisPlusConfig {

    @Value("${mybatis.page.size:500}")
    private long pageSize;

    @Autowired
    private MyBatisPlusTenantHandler mybatisPlusTenantHandler;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(getTenantLineInnerInterceptor()); // 多租户插件
        interceptor.addInnerInterceptor(this.getPageInterceptor()); // 分页插件，如果配置多个插件, 切记分页最后添加

        configuration.addInterceptor(interceptor);
        factory.setConfiguration(configuration);
        return factory.getObject();
    }

    /**
     * 分页插件配置
     */
    public PaginationInnerInterceptor getPageInterceptor() {
        // 分页插件，如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(pageSize);
        return paginationInnerInterceptor;
    }

    /**
     * 多租户插件配置
     */
    public TenantLineInnerInterceptor getTenantLineInnerInterceptor() {
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(mybatisPlusTenantHandler);
        return tenantInterceptor;
    }

}
