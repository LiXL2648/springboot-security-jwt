package com.zghky.waterCollection.config;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer(){

            @Override
            public void customize(Configuration configuration) {
                configuration.setMapUnderscoreToCamelCase(true);
                configuration.setJdbcTypeForNull(null);
                configuration.setAggressiveLazyLoading(false);
                configuration.setLazyLoadingEnabled(true);
                configuration.setCacheEnabled(false);
            }
        };
    }
}
