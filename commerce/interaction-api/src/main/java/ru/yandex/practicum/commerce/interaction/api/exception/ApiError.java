package ru.yandex.practicum.commerce.interaction.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiError {
    private String status;
    private String message;
    private String timestamp;
}
