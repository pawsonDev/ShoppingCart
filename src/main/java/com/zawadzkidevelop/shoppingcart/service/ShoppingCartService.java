package com.zawadzkidevelop.shoppingcart.service;

import com.zawadzkidevelop.shoppingcart.model.dto.ShoppingCartDTO;
import com.zawadzkidevelop.shoppingcart.model.entity.Product;
import com.zawadzkidevelop.shoppingcart.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Pawel on 2018-02-18.
 */

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartService {

    private final ProductRepository productRepository;

    private Map<String, Integer> products;

    @Autowired
    public ShoppingCartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.products = new HashMap<>();
    }

    public ShoppingCartDTO getAllProductsInCart() {
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setProducts(Collections.unmodifiableMap(products));
        shoppingCartDTO.setTotalAmountOfProducts(countAmountOfProductsInCart(products));
        shoppingCartDTO.setTotalPrice(countTotalPriceForProducts(products));
        return shoppingCartDTO;
    }

    private int countAmountOfProductsInCart(Map<String, Integer> products) {
        return products.values().stream().collect(Collectors.summingInt(v -> v.intValue()));
    }

    private BigDecimal countTotalPriceForProducts(Map<String, Integer> products) {
        return new BigDecimal(2.66);
    }

    public void addProductToCart(Long productId) {
        String productName = productRepository.findOne(productId).getName();
        if (products.containsKey(productName)) {
            products.replace(productName, products.get(productName) + 1);
        } else {
            products.put(productName, 1);
        }
    }

    public void removeProductFromCart(Long productId) {
        String productName = productRepository.findOne(productId).getName();
        if (products.containsKey(productName)) {
            if (products.get(productName) == 1) {
                products.remove(productName);
            } else {
                products.replace(productName, products.get(productName) - 1);
            }
        }
    }

    public Product searchProduct(String productName) {
        if (products.containsKey(productName)) {
            return productRepository.findProductByName(productName);
        }
        return null;
    }
}
