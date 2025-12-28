package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Entity(name = "ware_houses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WareHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "address_country")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "flat", column = @Column(name = "address_flat")),
    })
    private Address address;

    @ElementCollection
    @CollectionTable(name = "ware_house_products", joinColumns = @JoinColumn(name = "ware_house_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products;
}
