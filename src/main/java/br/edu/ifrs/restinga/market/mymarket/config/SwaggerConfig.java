package br.edu.ifrs.restinga.market.mymarket.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${springdoc.openapi.info.title}") String title,
            @Value("${springdoc.openapi.info.version}") String version,
            @Value("${springdoc.openapi.info.description}") String description
    ) {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description));
    }
}
