package ru.yandex.practicum.commerce.cart.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "cards")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    //@OneToMany(fetch = FetchType.LAZY)
    //products
}
