package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private UUID id;

    private boolean fragile;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "width", column = @Column(name = "dimension_width")),
            @AttributeOverride(name = "height", column = @Column(name = "dimension_height")),
            @AttributeOverride(name = "weight", column = @Column(name = "dimension_weight")),
    })
    private Dimension dimension;

    private Double weight;
}
