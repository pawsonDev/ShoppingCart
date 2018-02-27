package com.zawadzkidevelop.shoppingcart.controller;

import com.zawadzkidevelop.shoppingcart.model.dto.ShoppingCartDTO;
import com.zawadzkidevelop.shoppingcart.model.entity.Product;
import com.zawadzkidevelop.shoppingcart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Pawel on 2018-02-18.
 */

@RestController
@RequestMapping("/api/shoppingCart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ShoppingCartDTO> getShoppingCartSummary() {
        ShoppingCartDTO shoppingCartDTO = shoppingCartService.getShoppingCartSummary();
        return ResponseEntity.ok(shoppingCartDTO);
    }

    @RequestMapping(value = "/add/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<ShoppingCartDTO> addProductToCart(@PathVariable("productId") Long productId) {
        shoppingCartService.addProductToCart(productId);
        return getShoppingCartSummary();
    }

    @RequestMapping(value = "/remove/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<ShoppingCartDTO> removeProductFromCart(@PathVariable("productId") Long productId) {
        shoppingCartService.removeProductFromCart(productId);
        return getShoppingCartSummary();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> searchProduct(@RequestParam("name") String productName) {
        List<Product> products = shoppingCartService.searchProduct(productName);
        if (products != null && !products.isEmpty()) {
            return ResponseEntity.ok(products);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


}
