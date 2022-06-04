package com.example.useraccessdivide.common.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Class cấu hình document api
 * @author tranh
 *
 */
@Configuration
@EnableSwagger2
public class BaseSwaggerConfig {
	@Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }
    
    private ApiInfo apiEndPointsInfo(){
        return new ApiInfoBuilder().title("User Api")
                .description("Dự án api thương mại điện tử")
                .version("1.0")
                .contact(new Contact("Mạnh Hùng", "", "tranhung1997@gmail.com"))
                .build();
    }
}
