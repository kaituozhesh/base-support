package com.ktz.sh.base.config;

import com.ktz.sh.base.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName GlobalAutoConfiguration
 * @Description
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 10:22
 * @Version V1.0.0
 **/
@Configuration
public class GlobalAutoConfiguration {
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
