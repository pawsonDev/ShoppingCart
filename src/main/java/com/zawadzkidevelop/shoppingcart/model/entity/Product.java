package com.zawadzkidevelop.shoppingcart.model.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * Created by Pawel on 2018-02-18.
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    private String description;

    @DecimalMin(value = "0.00")
    private BigDecimal price;
}
