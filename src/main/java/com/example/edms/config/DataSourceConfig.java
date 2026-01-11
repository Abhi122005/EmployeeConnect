package com.example.edms.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DataSourceConfig {

    @Configuration
    @Profile("postgres")
    static class PostgresDataSourceConfig {
        @Bean
        public DataSource dataSource(@Value("${spring.datasource.url}") String url,
                                     @Value("${spring.datasource.username}") String user,
                                     @Value("${spring.datasource.password}") String pass) {
            return DataSourceBuilder.create()
                    .url(url)
                    .username(user)
                    .password(pass)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }
    }

}
