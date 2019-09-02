package com.siti.wisdomhydrologic.config;

import com.github.pagehelper.PageHelper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.util.Properties;

/**
 *
 * Created by DC on 2019/6/12.
 * mybatis 分页插件
 */
@Configuration
public class MybatisConfig {
    @Bean
    public PageHelper pageHelper() {
        // 分页插件pageHelper
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        //reasonable分页参数合理化，3.3.0以上版本可用，默认是false。
        // 启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页；
        // 禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据。
        properties.setProperty("reasonable", "false");
        properties.setProperty("helperDialigect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
    @Configuration
    @AutoConfigureAfter(MybatisConfig.class)
    public static class MyBatisMapper {

        @Bean
        public MapperHelper mapperHelper() {
            //配置通用mappers
            MapperHelper mapperHelper = new MapperHelper();
            Properties properties = new Properties();
            properties.setProperty("mappers", "tk.mybatis.mapper.common.Mapper");
            properties.setProperty("notEmpty", "false");
            properties.setProperty("IDENTITY", "MYSQL");
            mapperHelper.setProperties(properties);
            return mapperHelper;
        }

    }
}