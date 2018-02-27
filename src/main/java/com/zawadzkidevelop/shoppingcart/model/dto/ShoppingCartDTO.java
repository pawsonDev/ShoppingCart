package com.zawadzkidevelop.shoppingcart.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Pawel on 2018-02-18.
 */

@Setter
@Getter
@NoArgsConstructor
public class ShoppingCartDTO {

    private Map<String, Integer> products;

    private int totalAmountOfProducts;

    private BigDecimal totalPrice;
}
