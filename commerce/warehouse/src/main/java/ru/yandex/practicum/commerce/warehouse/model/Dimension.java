package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Builder
@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Dimension {
    private Double width;

    private Double height;

    private Double depth;
}
