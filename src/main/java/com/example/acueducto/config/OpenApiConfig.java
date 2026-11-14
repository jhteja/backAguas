package com.example.acueducto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Sistema de Facturación de Acueducto Veredal")
                        .version("1.0.0")
                        .description("API REST para la gestión de Suscriptores, Predios y Facturas del Acueducto Veredal con arquitectura por capas, DTOs (Records) y manejo global de excepciones.")
                        .contact(new Contact()
                                .name("Soporte Técnico Acueducto")
                                .email("soporte@acueducto.example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}