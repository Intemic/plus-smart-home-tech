package ru.yandex.practicum.commerce.order.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.commerce.interaction.api.exception.*;

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
                    case 400:
                        if (apiError.getExceptionClass() != null)
                            switch (apiError.getExceptionClass()) {
                                // WareHouseClient
                                case "ProductInShoppingCartLowQuantityInWarehouse":
                                    throw new ProductInShoppingCartLowQuantityInWarehouse(apiError.getMessage());
                                    // PaymentClient
                                case "NotEnoughInfoInOrderToCalculateException":
                                    throw new NotEnoughInfoInOrderToCalculateException(apiError.getMessage());
                            }
                        break;

                    case 404:
                        if (apiError.getExceptionClass() != null)
                            switch (apiError.getExceptionClass()) {
                                // WareHouseClient
                                case "NotFoundResource":
                                    throw new NotFoundResource(apiError.getMessage());
                                    // DeliveryClient
                                case "NoDeliveryFoundException":
                                    throw new NoDeliveryFoundException(apiError.getMessage());
                            }
                        break;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return defaultDecoder.decode(methodKey, response);
    }
}