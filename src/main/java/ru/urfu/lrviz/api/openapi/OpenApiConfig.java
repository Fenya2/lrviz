package ru.urfu.lrviz.api.openapi;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author fenya
 * @since 04.02.2026
 */
@Configuration
public class OpenApiConfig {

    public static final String DETAILED_API_DOCS_URL = "/api/docs/guide.html";

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("LR Visualizer API")
                        .version("1.0")
                        .description("API для построения LR автоматов")
                        .contact(
                                new Contact()
                                        .name("fenya")
                                        .email("fenya74.09@gmail.com")
                                        .url("https://t.me/fenya00"))
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("Подробная документация")
                        .url(DETAILED_API_DOCS_URL));
    }
}