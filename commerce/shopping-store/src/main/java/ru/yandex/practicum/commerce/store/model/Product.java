package ru.yandex.practicum.commerce.store.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductCategory;
import ru.yandex.practicum.commerce.interaction.api.enum_.ProductState;
import ru.yandex.practicum.commerce.interaction.api.enum_.QuantityState;

import java.util.UUID;

@Builder
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    private String description;

    @Column(name = "image_src")
    private String imageSrc;

    @Column(name = "quantity_state")
    @Enumerated(EnumType.STRING)
    private QuantityState quantityState;

    @Column(name = "product_state")
    @Enumerated(EnumType.STRING)
    private ProductState productState;

    @Column(name = "product_сategory")
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    private double price;


    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", quantityState=" + quantityState +
                ", productState=" + productState +
                ", productCategory=" + productCategory +
                ", price=" + price +
                '}';
    }
}
