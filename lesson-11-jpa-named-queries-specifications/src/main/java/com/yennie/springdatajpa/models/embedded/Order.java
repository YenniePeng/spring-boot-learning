package com.yennie.springdatajpa.models.embedded;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_order")
public class Order {
    @EmbeddedId
    private OrderId orderId;

    @Embedded
    private Address address;

    private String orderInfo;
    private String anotherField;
}
