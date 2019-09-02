package com.siti.wisdomhydrologic.config;

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