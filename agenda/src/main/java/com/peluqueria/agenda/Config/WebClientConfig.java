package com.peluqueria.agenda.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * Builder con @LoadBalanced: permite que Spring Cloud resuelva
     * "http://NOMBRE-SERVICIO" consultando el registro de Eureka
     * en lugar de hacer DNS normal.
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    // Nombre exacto: spring.application.name=disponibilidadProfesional
    @Bean
    public WebClient webClientDisponibilidad(WebClient.Builder builder) {
        return builder.baseUrl("http://disponibilidadProfesional").build();
    }

    // Nombre exacto: spring.application.name=tipoServicio
    @Bean
    public WebClient webClientTipoServicio(WebClient.Builder builder) {
        return builder.baseUrl("http://tipoServicio").build();
    }

    // Nombre exacto: spring.application.name=cliente
    @Bean
    public WebClient webClientCliente(WebClient.Builder builder) {
        return builder.baseUrl("http://cliente").build();
    }
}