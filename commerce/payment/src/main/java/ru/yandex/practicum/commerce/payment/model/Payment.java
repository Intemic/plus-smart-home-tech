package ru.yandex.practicum.commerce.payment.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.interaction.api.enum_.PaymentState;

import java.util.UUID;

@Builder
@Entity(name = "payments")
@Setter
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "delivery_price")
    private Double deliveryPrice;

    @Column(name = "product_price")
    private Double productPrice;

    @Enumerated(value = EnumType.STRING)
    private PaymentState state;

    @Transient
    private double taxRate;
}
