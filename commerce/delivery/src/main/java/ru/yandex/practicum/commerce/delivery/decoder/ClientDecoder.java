package ru.yandex.practicum.commerce.delivery.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.commerce.interaction.api.exception.ApiError;
import ru.yandex.practicum.commerce.interaction.api.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.interaction.api.exception.NotFoundResource;

import java.io.IOException;
import java.io.InputStream;

public class ClientDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper;

    public ClientDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        ApiError apiError = null;

        try {
            if (response.body() != null) {
                InputStream body = response.body().asInputStream();
                apiError = objectMapper.readValue(body, ApiError.class);

                switch (response.status()) {
                    // OrderClient
                    case 400:
                        if (apiError.getExceptionClass() != null
                                && apiError.getExceptionClass().equals("NoOrderFoundException"))
                            throw new NoOrderFoundException(apiError.getMessage());
                        break;

                    // WareHouseClient
                    case 404:
                        if (apiError.getExceptionClass() != null
                                && apiError.getExceptionClass().equals("NotFoundResource"))
                            throw new NotFoundResource(apiError.getMessage());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return defaultDecoder.decode(methodKey, response);
    }
}