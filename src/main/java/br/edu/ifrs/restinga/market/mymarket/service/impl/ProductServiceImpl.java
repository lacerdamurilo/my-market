package br.edu.ifrs.restinga.market.mymarket.service.impl;

import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductResponseDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductUpdateRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.entity.Product;
import br.edu.ifrs.restinga.market.mymarket.repository.ProductRepository;
import br.edu.ifrs.restinga.market.mymarket.repository.spec.ProductSpecification;
import br.edu.ifrs.restinga.market.mymarket.service.ProductService;
import br.edu.ifrs.restinga.market.mymarket.util.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductResponseDTO> findAll(Optional<String> name, Optional<String> type) {
        final var spec = ProductSpecification.builder()
                .name(name)
                .type(type)
                .build();
        return productRepository.findAll(spec).stream().map(ProductMapper::fromEntity).toList();
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, String.format("Product with id %s not found", id)));
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO request) {
        final var savedProduct = productRepository.save(ProductMapper.fromRequest(request));
        return ProductMapper.fromEntity(savedProduct);
    }

    @Override
    public void delete(String id) {
        final var product = findById(id);
        productRepository.delete(product);
    }

    @Override
    public ProductResponseDTO update(String id, ProductUpdateRequestDTO request) {
        var product = findById(id);
        product = product.toBuilder()
                .value(isNull(request.getValue()) ? product.getValue() : request.getValue())
                .quantity(isNull(request.getQuantity()) ? product.getQuantity() : request.getQuantity())
                .build();
        final var updatedProduct = productRepository.save(product);
        return ProductMapper.fromEntity(updatedProduct);
    }
}
