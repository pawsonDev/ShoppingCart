package com.zawadzkidevelop.shoppingcart.controller;

import com.zawadzkidevelop.shoppingcart.model.dto.ShoppingCartDTO;
import com.zawadzkidevelop.shoppingcart.model.entity.Product;
import com.zawadzkidevelop.shoppingcart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ShoppingCartDTO> getAllProductsInCart() {
        ShoppingCartDTO shoppingCartDTO = shoppingCartService.getAllProductsInCart();
        if (shoppingCartDTO != null) {
            return ResponseEntity.ok(shoppingCartDTO);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @RequestMapping(value = "/add/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<ShoppingCartDTO> addProductToCart(@PathVariable("productId") Long productId) {
        shoppingCartService.addProductToCart(productId);
        return getAllProductsInCart();
    }

    @RequestMapping(value = "/remove/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<ShoppingCartDTO> removeProductFromCart(@PathVariable("productId") Long productId) {
        shoppingCartService.removeProductFromCart(productId);
        return getAllProductsInCart();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Product> searchProduct(@RequestParam("name") String productName) {
        Product product = shoppingCartService.searchProduct(productName);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


}
