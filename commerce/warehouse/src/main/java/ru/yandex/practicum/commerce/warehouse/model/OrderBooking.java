package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Builder
@Entity(name = "order_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private UUID orderId;

    // для простоты считаем что один заказ - один склад
    @Column(name = "ware_house_id")
    private UUID wareHouseId;

    @Column(name = "delivery_id")
    private UUID deliveryId;

    @ElementCollection
    @CollectionTable(
            name = "order_booking_products",
            joinColumns = @JoinColumn(name = "order_booking_id", referencedColumnName = "id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> product;
}
