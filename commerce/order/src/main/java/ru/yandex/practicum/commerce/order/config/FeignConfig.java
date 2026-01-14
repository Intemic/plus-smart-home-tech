package ru.yandex.practicum.commerce.order.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.commerce.order.decoder.ClientDecoder;

public class FeignConfig {
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .errorDecoder(new ClientDecoder(new ObjectMapper()));
    }
}
