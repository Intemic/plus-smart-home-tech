package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Builder
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
            @AttributeOverride(name = "depth", column = @Column(name = "dimension_depth")),
    })
    private Dimension dimension;

    private Double weight;
}
