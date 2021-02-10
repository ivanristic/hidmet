package com.sargije.rest.hidmet.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableSwagger2
public class SwaggerConfig {
    /***
    It is not always desirable to expose the documentation for your entire API. You can restrict Swagger’s response by passing parameters to the apis() and paths() methods of the Docket class.
    PathSelectors provides additional filtering with predicates which scan the request paths of your application. You can use any(), none(), regex(), or ant().

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()                 
                .apis(RequestHandlerSelectors.basePackage("com.sargije.rest.hidmet.app.controller"))
                .paths(PathSelectors.ant("/api/v1/**"))
                .build()
                .apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder()
                .title("Weather Forecast API")
                .description("Weather Forecast API")
                .contact(new Contact("Ivan Ristić", "www.sargije.com", "ivanristich@hotmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }*/
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .pathsToMatch("/api/v1/**")
                .packagesToScan("com.sargije.rest.hidmet.app.controller")
                .group("hidmet")
                .build();
    }
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Weather Forecast API")
                        .description("Weather Forecast API")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
