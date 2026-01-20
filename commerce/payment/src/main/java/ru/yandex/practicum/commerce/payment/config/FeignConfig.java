package ru.yandex.practicum.commerce.payment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.commerce.payment.decoder.ClientDecoder;

@Configuration
public class FeignConfig {
    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .errorDecoder(new ClientDecoder(new ObjectMapper()));
    }
}
