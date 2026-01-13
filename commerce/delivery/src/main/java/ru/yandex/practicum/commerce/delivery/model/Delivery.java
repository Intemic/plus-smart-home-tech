package ru.yandex.practicum.commerce.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.interaction.api.enum_.DeliveryState;

import java.util.UUID;

@Builder
@Entity(name = "deliverys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID deliveryId;

    @Column(name = " order_id", nullable = false)
    private UUID orderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "from_country")),
            @AttributeOverride(name = "city", column = @Column(name = "from_city")),
            @AttributeOverride(name = "street", column = @Column(name = "from_street")),
            @AttributeOverride(name = "house", column = @Column(name = "from_house")),
            @AttributeOverride(name = "flat", column = @Column(name = "from_flat"))
    })
    private Address fromAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "to_country")),
            @AttributeOverride(name = "city", column = @Column(name = "to_city")),
            @AttributeOverride(name = "street", column = @Column(name = "to_street")),
            @AttributeOverride(name = "house", column = @Column(name = "to_house")),
            @AttributeOverride(name = "flat", column = @Column(name = "to_flat"))
    })
    private Address toAddress;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    private DeliveryState deliveryState;
}
