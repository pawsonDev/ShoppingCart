package com.zawadzkidevelop.shoppingcart.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Pawel on 2018-02-25.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addToCartWithSuccess() throws Exception {
        //GIVEN
        long tShirtId = 1;

        //WHEN
        ResultActions perform = mockMvc.perform(put("/api/shoppingCart/add/" + tShirtId).contentType(contentType));

        //THEN
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("T-shirt", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(32.99)));
    }

    @Test
    public void addToCartWhenProductIsAlreadyAdded() throws Exception {
        //GIVEN
        MockHttpSession mockSession = new MockHttpSession();
        long tShirtId = 1;

        //THEN
        mockMvc.perform(put("/api/shoppingCart/add/" + tShirtId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("T-shirt", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(32.99)));

        mockMvc.perform(put("/api/shoppingCart/add/" + tShirtId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("T-shirt", 2)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(2)))
                .andExpect(jsonPath("$.totalPrice", is(65.98)));
    }

    @Test
    public void addToCartFewProducts() throws Exception {
        //GIVEN
        MockHttpSession mockSession = new MockHttpSession();
        long tShirtId = 1;
        long jacketId = 2;

        //THEN
        mockMvc.perform(put("/api/shoppingCart/add/" + tShirtId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("T-shirt", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(32.99)));

        mockMvc.perform(put("/api/shoppingCart/add/" + jacketId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("T-shirt", 1)))
                .andExpect(jsonPath("$.products", hasEntry("Jacket", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(2)))
                .andExpect(jsonPath("$.totalPrice", is(162.98)));

        mockMvc.perform(put("/api/shoppingCart/add/" + jacketId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("T-shirt", 1)))
                .andExpect(jsonPath("$.products", hasEntry("Jacket", 2)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(3)))
                .andExpect(jsonPath("$.totalPrice", is(292.97)));
    }

    @Test
    public void removeFromCartOneProduct() throws Exception {
        //GIVEN
        MockHttpSession mockSession = new MockHttpSession();
        long jacketId = 2;

        //THEN
        mockMvc.perform(put("/api/shoppingCart/add/" + jacketId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("Jacket", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(129.99)));

        mockMvc.perform(put("/api/shoppingCart/remove/" + jacketId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", not(hasEntry("Jacket", 1))))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(0)))
                .andExpect(jsonPath("$.totalPrice", is(0)));
    }

    @Test
    public void removeFromCartOnlyOneProduct() throws Exception {
        //GIVEN
        MockHttpSession mockSession = new MockHttpSession();
        long jacketId = 2;
        long jeansId = 3;

        //THEN
        mockMvc.perform(put("/api/shoppingCart/add/" + jacketId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("Jacket", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(129.99)));

        mockMvc.perform(put("/api/shoppingCart/add/" + jeansId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("Jacket", 1)))
                .andExpect(jsonPath("$.products", hasEntry("Jeans", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(2)))
                .andExpect(jsonPath("$.totalPrice", is(209.98)));

        mockMvc.perform(put("/api/shoppingCart/remove/" + jacketId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("Jeans", 1)))
                .andExpect(jsonPath("$.products", not(hasEntry("Jacket", 1))))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(79.99)));
    }

    @Test
    public void getShoppingCartSummaryWhenCartIsEmpty() throws Exception {
        //WHEN
        ResultActions perform = mockMvc.perform(get("/api/shoppingCart").contentType(contentType));

        //THEN
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmountOfProducts", is(0)))
                .andExpect(jsonPath("$.totalPrice", is(0)));
    }

    @Test
    public void searchProductByName() throws Exception {
        //GIVEN
        MockHttpSession mockSession = new MockHttpSession();
        long jacketId = 2;
        String name = "Jacket";

        //THEN
        mockMvc.perform(put("/api/shoppingCart/add/" + jacketId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("Jacket", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(129.99)));

        mockMvc.perform(get("/api/shoppingCart/search?name=" + name).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is(name)))
                .andExpect(jsonPath("$[0].description", is("Spring leather jacket")))
                .andExpect(jsonPath("$[0].price", is(129.99)));

    }

    @Test
    public void searchProductByNameWhenNameIsLowerCase() throws Exception {
        //GIVEN
        MockHttpSession mockSession = new MockHttpSession();
        long jacketId = 2;
        String name = "jacket";

        //THEN
        mockMvc.perform(put("/api/shoppingCart/add/" + jacketId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("Jacket", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(129.99)));

        mockMvc.perform(get("/api/shoppingCart/search?name=" + name).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Jacket")))
                .andExpect(jsonPath("$[0].description", is("Spring leather jacket")))
                .andExpect(jsonPath("$[0].price", is(129.99)));

    }

    @Test
    public void searchProductByNameWhenGivenIsPartOfName() throws Exception {
        //GIVEN
        MockHttpSession mockSession = new MockHttpSession();
        long jacketId = 2;
        long jeansId = 3;
        String name = "j";

        //THEN
        mockMvc.perform(put("/api/shoppingCart/add/" + jacketId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("Jacket", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(129.99)));

        mockMvc.perform(put("/api/shoppingCart/add/" + jeansId).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasEntry("Jacket", 1)))
                .andExpect(jsonPath("$.products", hasEntry("Jeans", 1)))
                .andExpect(jsonPath("$.totalAmountOfProducts", is(2)))
                .andExpect(jsonPath("$.totalPrice", is(209.98)));

        mockMvc.perform(get("/api/shoppingCart/search?name=" + name).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(3)))
                .andExpect(jsonPath("$[0].name", is("Jeans")))
                .andExpect(jsonPath("$[0].description", is("Blue jeans for man")))
                .andExpect(jsonPath("$[0].price", is(79.99)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Jacket")))
                .andExpect(jsonPath("$[1].description", is("Spring leather jacket")))
                .andExpect(jsonPath("$[1].price", is(129.99)));

    }

    @Test
    public void searchProductByNameWhenProductNotFound() throws Exception {
        //GIVEN
        MockHttpSession mockSession = new MockHttpSession();
        String name = "Jacket";

        //THEN
        mockMvc.perform(get("/api/shoppingCart/search?name=" + name).contentType(contentType)
                .session(mockSession))
                .andExpect(status().isNotFound());

    }
}
