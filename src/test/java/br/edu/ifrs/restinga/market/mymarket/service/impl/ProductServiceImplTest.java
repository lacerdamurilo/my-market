package br.edu.ifrs.restinga.market.mymarket.service.impl;

import br.edu.ifrs.restinga.market.mymarket.model.entity.Product;
import br.edu.ifrs.restinga.market.mymarket.repository.ProductRepository;
import br.edu.ifrs.restinga.market.mymarket.repository.spec.ProductSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static br.edu.ifrs.restinga.market.mymarket.creator.product.ProductCreator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productServiceImpl;
    @Mock
    private ProductRepository productRepository;

    @Test
    void findAll_returnsEmptyList_whenNoObjectsWereSaved() {
        // SETUP
        final var invalidName = Optional.of("invalidName");
        final var invalidType = Optional.of("invalidType");

        // MOCK DE COMPORTAMENTO
        when(productRepository.findAll(isA(ProductSpecification.class))).thenReturn(List.of());

        // RODAR O SERVIÇO
        final var result = productServiceImpl.findAll(invalidName, invalidType);

        // CONFERIR SE O RESULTADO É O ESPERADO
        assertThat(result).isEmpty();
    }

    @Test
    void findAll_returnsListOfProducts_whenSuccessful() {
        // MOCK DE COMPORTAMENTO
        when(productRepository.findAll(isA(ProductSpecification.class))).thenReturn(List.of(PRODUCT_1, PRODUCT_2));

        // RODAR O SERVIÇO
        final var result = productServiceImpl.findAll(Optional.empty(), Optional.empty());

        // CONFERIR SE O RESULTADO É O ESPERADO
        assertEquals(List.of(PRODUCT_RESPONSE_DTO_1, PRODUCT_RESPONSE_DTO_2), result);
    }

    @Test
    void findById_returnsAProduct_whenSuccessful() {
        // MOCK DE COMPORTAMENTO
        when(productRepository.findById(PRODUCT_1.getId())).thenReturn(Optional.of(PRODUCT_1));

        // RODAR O SERVIÇO
        final var result = productServiceImpl.findById(PRODUCT_1.getId());

        // CONFERIR SE O RESULTADO É O ESPERADO
        assertEquals(PRODUCT_1, result);
    }

    @Test
    void findById_returnsError_whenProductIsNotFound() {
        // RODAR O SERVIÇO E CONFERIR SE O TIPO CORRETO DE EXCEÇÃO FOI LANÇADO
        final var result = assertThrows(ResponseStatusException.class, () -> productServiceImpl.findById("3"));

        // CONFERIR SE A MENSAGEM DE ERRO ESTÁ CORRETA
        assertEquals("404 NOT_FOUND \"Product with id 3 not found\"", result.getMessage());
    }

    @Test
    void create_returns201_whenSuccessfulCreated() {
        // MOCK DE COMPORTAMENTO
        when(productRepository.save(isA(Product.class))).thenReturn(PRODUCT_1);

        // RODAR O SERVIÇO
        final var result = productServiceImpl.create(PRODUCT_REQUEST_DTO_1);

        // CONFERIR SE O RESULTADO É O ESPERADO
        assertEquals(PRODUCT_RESPONSE_DTO_1, result);
    }
}
