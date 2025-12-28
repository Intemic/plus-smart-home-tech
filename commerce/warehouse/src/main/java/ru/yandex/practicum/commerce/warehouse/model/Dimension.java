package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Embeddable
public class Dimension {
    private Double width;

    private Double height;

    private Double depth;
}
