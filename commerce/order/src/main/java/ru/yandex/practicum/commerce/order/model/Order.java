package ru.yandex.practicum.commerce.order.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.interaction.api.enum_.OrderState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Builder
@Entity(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "cart_id", nullable = false)
    private UUID shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "orders_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products = new HashMap<>();

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @Enumerated(value = EnumType.STRING)
    private OrderState state;

    @Column(name = "delivery_weight")
    private Double deliveryWeight;

    @Column(name = "delivery_volume")
    private Double deliveryVolume;

    private boolean fragile;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "delivery_price")
    private Double deliveryPrice;

    @Column(name = "product_price")
    private Double productPrice;
}
