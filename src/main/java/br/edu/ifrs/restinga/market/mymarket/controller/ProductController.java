package br.edu.ifrs.restinga.market.mymarket.controller;

import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductResponseDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductUpdateRequestDTO;

import java.util.List;
import java.util.Optional;

public interface ProductController {

    List<ProductResponseDTO> findAll(Optional<String> name, Optional<String> type);

    ProductResponseDTO create(ProductRequestDTO request);

    void delete(String id);

    ProductResponseDTO update(String id, ProductUpdateRequestDTO request);
}
