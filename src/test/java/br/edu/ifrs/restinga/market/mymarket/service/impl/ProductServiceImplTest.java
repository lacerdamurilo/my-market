package br.edu.ifrs.restinga.market.mymarket.service.impl;

import br.edu.ifrs.restinga.market.mymarket.model.entity.Product;
import br.edu.ifrs.restinga.market.mymarket.repository.ProductRepository;
import br.edu.ifrs.restinga.market.mymarket.repository.spec.ProductSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static br.edu.ifrs.restinga.market.mymarket.creator.product.ProductCreator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

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
        final var captor = ArgumentCaptor.forClass(ProductSpecification.class);

        // MOCK DE COMPORTAMENTO
        when(productRepository.findAll(captor.capture())).thenReturn(List.of());

        // RODAR O SERVIÇO
        final var result = productServiceImpl.findAll(invalidName, invalidType);

        // CONFERIR SE O RESULTADO É O ESPERADO
        assertThat(result).isEmpty();
    }

    @Test
    void findAll_returnsListOfProducts_whenSuccessful() {
        // Setup
        final var captor = ArgumentCaptor.forClass(ProductSpecification.class);

        // MOCK DE COMPORTAMENTO
        when(productRepository.findAll(captor.capture())).thenReturn(List.of(PRODUCT_1, PRODUCT_2));

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
        // Setup
        final var captor = ArgumentCaptor.forClass(Product.class);

        // MOCK DE COMPORTAMENTO
        when(productRepository.save(captor.capture())).thenReturn(PRODUCT_1);

        // RODAR O SERVIÇO
        final var result = productServiceImpl.create(PRODUCT_REQUEST_DTO_1);

        // CONFERIR SE O RESULTADO É O ESPERADO
        assertEquals(PRODUCT_RESPONSE_DTO_1, result);
    }

    @Test
    void create_returns400_whenBadRequest() {
        // Setup
        final var captor = ArgumentCaptor.forClass(Product.class);

        // Configurar o comportamento do repository, que não será chamado, já que esperamos que o serviço lance a exceção.
        when(productRepository.save(captor.capture())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product data"));

        // Rodar o serviço e verificar se a exceção é lançada com o código de status correto (400 Bad Request)
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            productServiceImpl.create(PRODUCT_REQUEST_DTO_3);
        });

        System.out.println("Thrown exception message: " + thrown.getMessage());

        // Verificar se o código de status é 400 e a mensagem está correta
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Invalid product data", thrown.getReason());
    }

    @Test
    void update_returns200_whenSuccessful() {
        // Setup
        final var captor = ArgumentCaptor.forClass(Product.class);

        // Mock de comportamento: quando buscar um produto pelo ID, retorna um produto existente
        when(productRepository.findById(PRODUCT_1.getId())).thenReturn(Optional.of(PRODUCT_1));

        // Mock de comportamento: simula a atualização do produto no repositório
        when(productRepository.save(captor.capture())).thenReturn(PRODUCT_1.toBuilder()
                .value(PRODUCT_UPDATE_REQUEST_DTO_4.getValue())
                .quantity(PRODUCT_UPDATE_REQUEST_DTO_4.getQuantity())
                .build());

        // Rodar o serviço
        final var result = productServiceImpl.update(PRODUCT_1.getId(), PRODUCT_UPDATE_REQUEST_DTO_4);

        // Verificar se o resultado é o esperado
        assertEquals(PRODUCT_RESPONSE_DTO_1, result);
    }

    @Test
    void update_returns404_whenProductNotFound() {
        // Mock de comportamento: quando buscar um produto pelo ID, o repositório retorna Optional.empty (produto não encontrado)
        when(productRepository.findById("3")).thenReturn(Optional.empty());

        // Rodar o serviço e verificar se a exceção é lançada
        final var result = assertThrows(ResponseStatusException.class, () -> productServiceImpl.update("3", PRODUCT_UPDATE_REQUEST_DTO_5));

        // Verificar se a mensagem de erro está correta (produto não encontrado)
        assertEquals("404 NOT_FOUND \"Product with id 3 not found\"", result.getMessage());
    }

    @Test
    void delete_returns200_whenSuccessful() {
        // Setup
        final var captor = ArgumentCaptor.forClass(Product.class);

        // Mock de comportamento: quando buscar um produto pelo ID, retorna um produto existente
        when(productRepository.findById(PRODUCT_1.getId())).thenReturn(Optional.of(PRODUCT_1));

        // Simula que o method delete não faz nada (como é void)
        doNothing().when(productRepository).delete(captor.capture());  // Não é necessário retornar nada

        // Rodar o serviço e verificar se o method de delete é chamado sem exceção
        productServiceImpl.delete(PRODUCT_1.getId());

        // Como não esperamos retorno do method delete (é void), vamos garantir que o method delete foi chamado
        // Verifique se o method findById e delete foram chamados
        verify(productRepository).findById(PRODUCT_1.getId());
        verify(productRepository).delete(PRODUCT_1);
    }

    @Test
    void delete_returns404_whenProductNotFound() {
        // Mock de comportamento: quando buscar um produto pelo ID, retorna Optional.empty (produto não encontrado)
        when(productRepository.findById("3")).thenReturn(Optional.empty());

        // Rodar o serviço e verificar se a exceção é lançada
        final var result = assertThrows(ResponseStatusException.class, () -> productServiceImpl.delete("3"));

        // Verificar se a mensagem de erro está correta (produto não encontrado)
        assertEquals("404 NOT_FOUND \"Product with id 3 not found\"", result.getMessage());
    }
}
