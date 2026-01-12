package ru.yandex.practicum.commerce.interaction.api.exception;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String status;
    private String message;
    private String timestamp;
    private String exceptionClass;
}
