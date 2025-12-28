package ru.yandex.practicum.commerce.cart.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.commerce.interaction.api.enum_.CartState;

import java.util.Map;
import java.util.UUID;

@Builder
@Entity(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Enumerated(value = EnumType.STRING)
    CartState state;

    @ElementCollection
    @CollectionTable(name = "cart_products", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products;
}
