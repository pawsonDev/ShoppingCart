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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Pawel on 2018-02-18.
 */

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCartService {

    private final ProductRepository productRepository;

    private Map<Product, Integer> products;

    @Autowired
    public ShoppingCartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.products = new HashMap<>();
    }

    public ShoppingCartDTO getShoppingCartSummary() {
        ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setProducts(getProductsInCart(products));
        shoppingCartDTO.setTotalAmountOfProducts(countAmountOfProductsInCart(products));
        shoppingCartDTO.setTotalPrice(countTotalPriceForProducts(products));
        return shoppingCartDTO;
    }

    private Map<String, Integer> getProductsInCart(Map<Product, Integer> products) {
        return products.entrySet().stream().collect(Collectors.toMap(k -> k.getKey().getName(), v -> v.getValue()));
    }

    private int countAmountOfProductsInCart(Map<Product, Integer> products) {
        return products.values().stream().collect(Collectors.summingInt(v -> v.intValue()));
    }

    private BigDecimal countTotalPriceForProducts(Map<Product, Integer> products) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            total = total.add(entry.getKey().getPrice().multiply(new BigDecimal(entry.getValue())));
        }
        return total;
    }

    public void addProductToCart(Long productId) {
        Product productToAdd = productRepository.findOne(productId);
        if (products.containsKey(productToAdd)) {
            products.replace(productToAdd, products.get(productToAdd) + 1);
        } else {
            products.put(productToAdd, 1);
        }
    }

    public void removeProductFromCart(Long productId) {
        Product productToRemove = productRepository.findOne(productId);
        if (products.containsKey(productToRemove)) {
            if (products.get(productToRemove) == 1) {
                products.remove(productToRemove);
            } else {
                products.replace(productToRemove, products.get(productToRemove) - 1);
            }
        }
    }

    public List<Product> searchProduct(String productName) {
        List<Product> searchResult = new ArrayList<>();
        for (Product product : products.keySet()) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                searchResult.add(product);
            }
        }
        return searchResult;
    }
}
