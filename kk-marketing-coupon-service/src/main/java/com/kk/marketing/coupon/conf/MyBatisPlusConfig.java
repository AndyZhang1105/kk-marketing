package com.kk.marketing.coupon.conf;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.kk.marketing.coupon.mapper"})
public class MyBatisPlusConfig {

	@Value("${mybatis.page.size:500}")
	private long pageSize;

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);

		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setMapUnderscoreToCamelCase(true);

		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

		// 分页插件，如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
		paginationInnerInterceptor.setMaxLimit(pageSize);
		interceptor.addInnerInterceptor(paginationInnerInterceptor); // 如果配置多个插件, 切记分页最后添加

		configuration.addInterceptor(interceptor);

		factory.setConfiguration(configuration);
		return factory.getObject();
	}

}
