package com.zawadzkidevelop.shoppingcart.model.repository;

import com.zawadzkidevelop.shoppingcart.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Pawel on 2018-02-18.
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product as p WHERE p.name = :productName")
    Product findProductByName(@Param("productName") String productName);
}
