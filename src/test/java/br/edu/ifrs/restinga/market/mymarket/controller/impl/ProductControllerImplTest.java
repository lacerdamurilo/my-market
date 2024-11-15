package br.edu.ifrs.restinga.market.mymarket.controller.impl;

import com.jayway.jsonpath.JsonPath;
import io.vavr.control.Try;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static br.edu.ifrs.restinga.market.mymarket.creator.product.ProductCreator.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerImplTest {


    @Autowired
    private MockMvc mockMvc;

    public static final String USER = "admin";
    public static final String PASS = "password";
    public static String ID = "";

    @Test
    @Order(1)
    void find_returnsEmptyList_whenNoProductsAreFound() {
        Try.run(
                () -> mockMvc.perform(
                                get("/products/find")
                                        .param("name", "invalid_name")
                                        .param("type", "invalid_type")
                        )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$").isEmpty())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(3)
    void find_ListOfProducts_whenSuccessful() {
        Try.run(
                () -> mockMvc.perform(
                                get("/products/find")
                                        .param("name", PRODUCT_1.getName())
                                        .param("type", PRODUCT_1.getType())
                        )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$[0].id").value(ID))
                        .andExpect(jsonPath("$[0].name").value(PRODUCT_RESPONSE_DTO_1.getName()))
                        .andExpect(jsonPath("$[0].type").value(PRODUCT_RESPONSE_DTO_1.getType()))
                        .andExpect(jsonPath("$[0].value").value(PRODUCT_RESPONSE_DTO_1.getValue()))
                        .andExpect(jsonPath("$[0].quantity").value(PRODUCT_RESPONSE_DTO_1.getQuantity()))
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(2)
    void create_savesProduct_whenSuccessful() {
        Try.run(
                () -> {
                    final var response = mockMvc.perform(
                                    post("/products/create")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(PRODUCT_REQUEST_JSON_1)
                                            .with(httpBasic(USER, PASS))
                            )
                            .andExpect(status().isCreated())
                            .andExpect(jsonPath("$.name").value(PRODUCT_RESPONSE_DTO_1.getName()))
                            .andExpect(jsonPath("$.type").value(PRODUCT_RESPONSE_DTO_1.getType()))
                            .andExpect(jsonPath("$.value").value(PRODUCT_RESPONSE_DTO_1.getValue()))
                            .andExpect(jsonPath("$.quantity").value(PRODUCT_RESPONSE_DTO_1.getQuantity()))
                            .andReturn()
                            .getResponse()
                            .getContentAsString();
                    ID = JsonPath.parse(response).read("$.id", String.class);
                }
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(4)
    void create_returns401_whenCredentialsAreInvalid() {
        Try.run(
                () -> mockMvc.perform(
                                post("/products/create")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(PRODUCT_REQUEST_JSON_1)
                                        .with(httpBasic("user", "user"))
                        )
                        .andExpect(status().isUnauthorized())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(10)
    void delete_returns204_whenSuccessful() {
        Try.run(
                () -> mockMvc.perform(
                                delete("/products/delete/{id}", ID)
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isNoContent())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(9)
    void delete_returns401_whenNotLogged() {
        Try.run(
                () -> mockMvc.perform(delete("/products/delete/{id}", ID))
                        .andExpect(status().isUnauthorized())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(11)
    void delete_returns404_whenProductIsNotFound() {
        Try.run(
                () -> mockMvc.perform(
                                delete("/products/delete/{id}", ID)
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isNotFound())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(6)
    void update_updatesProduct_whenSuccessful() {
        Try.run(
                () -> mockMvc.perform(
                                put("/products/update/{id}", ID)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(PRODUCT_REQUEST_JSON_2)
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(ID))
                        .andExpect(jsonPath("$.name").value(PRODUCT_RESPONSE_DTO_1.getName()))
                        .andExpect(jsonPath("$.type").value(PRODUCT_RESPONSE_DTO_1.getType()))
                        .andExpect(jsonPath("$.value").value(PRODUCT_RESPONSE_DTO_2.getValue()))
                        .andExpect(jsonPath("$.quantity").value(PRODUCT_RESPONSE_DTO_2.getQuantity()))
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(5)
    void update_updatesProduct_whenValueAndQuantityNotInformed() {
        Try.run(
                () -> mockMvc.perform(
                                put("/products/update/{id}", ID)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(PRODUCT_REQUEST_JSON_3)
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(ID))
                        .andExpect(jsonPath("$.name").value(PRODUCT_RESPONSE_DTO_1.getName()))
                        .andExpect(jsonPath("$.type").value(PRODUCT_RESPONSE_DTO_1.getType()))
                        .andExpect(jsonPath("$.value").value(PRODUCT_RESPONSE_DTO_1.getValue()))
                        .andExpect(jsonPath("$.quantity").value(PRODUCT_RESPONSE_DTO_1.getQuantity()))
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(7)
    void update_returns401_whenNotLogged() {
        Try.run(
                () -> mockMvc.perform(
                                put("/products/update/{id}", ID)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(PRODUCT_REQUEST_JSON_1)
                        )
                        .andExpect(status().isUnauthorized())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    @Order(8)
    void update_returns404_whenProductIsNotFound() {
        Try.run(
                () -> mockMvc.perform(
                                put("/products/update/{id}", "invalid")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(PRODUCT_REQUEST_JSON_1)
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isNotFound())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }
}
