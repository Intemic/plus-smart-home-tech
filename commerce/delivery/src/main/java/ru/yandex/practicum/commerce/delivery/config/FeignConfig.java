package ru.yandex.practicum.commerce.delivery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.commerce.delivery.decoder.ClientDecoder;

@Configuration
public class FeignConfig {
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .errorDecoder(new ClientDecoder(new ObjectMapper()));
    }
}
