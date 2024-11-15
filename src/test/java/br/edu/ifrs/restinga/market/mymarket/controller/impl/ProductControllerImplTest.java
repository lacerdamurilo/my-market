package br.edu.ifrs.restinga.market.mymarket.controller.impl;

import br.edu.ifrs.restinga.market.mymarket.model.entity.Product;
import br.edu.ifrs.restinga.market.mymarket.repository.ProductRepository;
import br.edu.ifrs.restinga.market.mymarket.repository.spec.ProductSpecification;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static br.edu.ifrs.restinga.market.mymarket.creator.product.ProductCreator.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerImplTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    public static final String USER = "admin";
    public static final String PASS = "password";

    @Test
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
    void find_ListOfProducts_whenSuccessful() {
        final var captor = ArgumentCaptor.forClass(ProductSpecification.class);

        when(productRepository.findAll(captor.capture())).thenReturn(List.of(PRODUCT_1, PRODUCT_2));

        Try.run(
                () -> mockMvc.perform(get("/products/find"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$[0]").value(PRODUCT_RESPONSE_DTO_1))
                        .andExpect(jsonPath("$[1]").value(PRODUCT_RESPONSE_DTO_2))
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    void create_savesProduct_whenSuccessful() {
        final var captor = ArgumentCaptor.forClass(Product.class);

        when(productRepository.save(captor.capture())).thenReturn(PRODUCT_1);

        Try.run(
                () -> mockMvc.perform(
                                post("/products/create")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(PRODUCT_REQUEST_JSON_1)
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$").value(PRODUCT_RESPONSE_DTO_1))
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
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
    void delete_returns204_whenSuccessful() {
        when(productRepository.findById(PRODUCT_1.getId())).thenReturn(Optional.of(PRODUCT_1));
        Try.run(
                () -> mockMvc.perform(
                                delete("/products/delete/{id}", PRODUCT_1.getId())
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isNoContent())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    void delete_returns401_whenNotLogged() {
        Try.run(
                () -> mockMvc.perform(delete("/products/delete/{id}", PRODUCT_1.getId()))
                        .andExpect(status().isUnauthorized())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    void delete_returns404_whenProductIsNotFound() {
        Try.run(
                () -> mockMvc.perform(
                                delete("/products/delete/{id}", "invalid")
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isNotFound())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    void update_updatesProduct_whenSuccessful() {
        when(productRepository.findById(PRODUCT_1.getId())).thenReturn(Optional.of(PRODUCT_1));
        when(productRepository.save(PRODUCT_1)).thenReturn(PRODUCT_1);
        Try.run(
                () -> mockMvc.perform(
                                put("/products/update/{id}", PRODUCT_1.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(PRODUCT_REQUEST_JSON_1)
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isOk())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    void update_updatesProduct_whenValueAndQuantityNotInformed() {
        when(productRepository.findById(PRODUCT_1.getId())).thenReturn(Optional.of(PRODUCT_1));
        when(productRepository.save(PRODUCT_1)).thenReturn(PRODUCT_1);
        Try.run(
                () -> mockMvc.perform(
                                put("/products/update/{id}", PRODUCT_1.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(PRODUCT_REQUEST_JSON_2)
                                        .with(httpBasic(USER, PASS))
                        )
                        .andExpect(status().isOk())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    void update_returns401_whenNotLogged() {
        Try.run(
                () -> mockMvc.perform(
                                put("/products/update/{id}", PRODUCT_1.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(PRODUCT_REQUEST_JSON_1)
                        )
                        .andExpect(status().isUnauthorized())
        ).onFailure(throwable -> {
            throw new RuntimeException(throwable);
        });
    }

    @Test
    void update_returns404_whenProductIsNotFound() {
        Try.run(
                () -> mockMvc.perform(
                                put("/products/update/{id}", PRODUCT_1.getId())
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
