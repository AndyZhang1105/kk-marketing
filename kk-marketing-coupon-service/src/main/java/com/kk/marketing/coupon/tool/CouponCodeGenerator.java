package com.kk.marketing.coupon.tool;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.kk.marketing.coupon.entity.BaseEntity;
import com.kk.marketing.coupon.service.BaseService;
import com.kk.marketing.coupon.service.impl.BaseServiceImpl;

import java.sql.Types;
import java.util.List;

/**
 * @author Zal
 */
public class CouponCodeGenerator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/kk_marketing?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";
        String username = "kk";
        String password = "g=utf8&zeroD";

        List<String> tables = List.of("t_coupon_verifier");//需要生成对应代码的表名的集合

        FastAutoGenerator.create(url, username, password)

                         .globalConfig(builder -> {
                             builder.author("Zal") // 设置作者
                                    .disableOpenDir().enableSpringdoc().enableSwagger() // 开启 swagger 模式
                                    .outputDir(System.getProperty("user.dir") + "\\kk-marketing-coupon-service\\src\\main\\java") // 指定输出目录
                                    .commentDate("yyyy-MM-dd");//日期格式
                         })

                         .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                             int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                             if (typeCode == Types.SMALLINT) {
                                 // 自定义类型转换
                                 return DbColumnType.INTEGER;
                             }
                             return typeRegistry.getColumnType(metaInfo);
                         }))

                         //包名配置--------------------------------------------------------------------------------------------
                         .packageConfig(builder -> builder.parent("com.kk.marketing.coupon") // 设置父包名
                                                          // .moduleName("system") // 设置父包模块名，设置该项会在输出路径上增加一层模块名目录
                                                          .entity("entity").mapper("mapper").xml("mapper").service("service").serviceImpl("service.impl")

                                 //策略配置-----------------------------------------------------------------------------------------
                         ).strategyConfig(builder -> builder.addInclude(tables) // 设置需要生成的表名
                                                            //.addTablePrefix("t_", "c_") // 设置过滤表前缀

                                                            //实体策略配置
                                                            .entityBuilder()
                                                            .enableFileOverride()// TODO 开启覆盖已生成的entity文件,关闭则注释本行
                                                            .enableLombok()// 自动添加lombok注解@Getter @Setter
                                                            .logicDeleteColumnName("deleted")// 指定逻辑删除字段名自动为其添加逻辑删除字段注解
                                                            //.enableTableFieldAnnotation()//启用表字段注解@TableField
                                                            //.enableRemoveIsPrefix()
                                                            .disableSerialVersionUID()//
                                                            .addIgnoreColumns("tenant_id", "update_time", "create_time", "update_by", "create_by", "deleted") //
                                                            .convertFileName(o -> o.substring(1))//
                                                            .superClass(BaseEntity.class)
                                                            .javaTemplate("/templates/entity.java2") // make sure this file is generated within target directory

                                                            //Mapper策略配置
                                                            .mapperBuilder()
                                                            //.enableBaseResultMap() // 生成通用的resultMap
                                                            .superClass(BaseMapper.class) //
                                                            .formatMapperFileName("%sMapper")//mapper文件后缀,如UserMapper
                                                            // .enableFileOverride()// TODO 开启覆盖已生成的mapper文件,关闭则注释本行
                                                            .formatMapperFileName("%sMapper")//mapper文件后缀,如UserMapper
                                                            .formatXmlFileName("%sMapper")//xml文件后缀,如UserMapper.xml
                                                            .convertXmlFileName(o -> o.substring(1) + "Mapper")
                                                            .convertMapperFileName(o -> o.substring(1) + "Mapper") //

                                                            //Service策略配置
                                                            .serviceBuilder()
                                                            // .enableFileOverride()//TODO 开启覆盖已生成的service文件,关闭则注释本行
                                                            .convertServiceFileName((o) -> o.substring(1))
                                                            .formatServiceFileName("%sService")
                                                            .formatServiceImplFileName("%sServiceImpl")
                                                            .convertServiceFileName(o -> o.substring(1) + "Service")
                                                            .convertServiceImplFileName(o -> o.substring(1) + "ServiceImpl")
                                                            .superServiceClass(BaseService.class)
                                                            .superServiceImplClass(BaseServiceImpl.class)

                                                            //Controller策略配置
                                                            .controllerBuilder().disable()

                         ).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                         .execute();
    }

}
