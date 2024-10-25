package com.me.clouddrive.config;

import com.me.clouddrive.mapper.impl.MinioItemMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperBeans {

    @Bean
    public MinioItemMapperImpl minioItemMapper() {
        return new MinioItemMapperImpl();
    }
}
