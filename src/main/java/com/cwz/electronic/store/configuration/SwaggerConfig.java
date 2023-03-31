package com.cwz.electronic.store.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket docket(){
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());

        docket.securityContexts(Arrays.asList(getSecurityContext()));
        docket.securitySchemes(Arrays.asList(getSchemes()));

        ApiSelectorBuilder select = docket.select();
        select.apis(RequestHandlerSelectors.any());
        select.paths(PathSelectors.any());
        Docket build = select.build();
        return build;
    }

    private ApiKey getSchemes() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext getSecurityContext() {
        SecurityContext context = SecurityContext
                .builder()
                .securityReferences(getSecurityReferences())
                .build();
        return context;
    }

    private List<SecurityReference> getSecurityReferences() {
        AuthorizationScope [] scopes = {
                new AuthorizationScope("Global", "Access evrything")
        };
        return Arrays.asList(new SecurityReference("JWT", scopes));
    }

    private ApiInfo getApiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Electronic Store Backend : APIS",
                "This is backend project created by ZiaulHoque",
                "1.0.0V",
                "https://github.com/ziaul9694?tab=repositories",
                new Contact("Ziaul","https://www.linkedin.com/in/ziaul-hoque-b308b9270/","ziaul9846@gmail.com"),
                "License of APIS",
                "https://www.linkedin.com/in/ziaul-hoque-b308b9270/",
                new ArrayList<>()
        );
        return apiInfo;
    }
}
