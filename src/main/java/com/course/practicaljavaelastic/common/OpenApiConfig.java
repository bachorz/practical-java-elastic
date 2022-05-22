package com.course.practicaljavaelastic.common;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI practicalJavaElasticOpenApi(){
        Info info = new Info().title("Practical Java API").description("OpenApi (Swagger) documentation auto generated from code").version("1.0");

        return new OpenAPI().info(info);
    }
}
