package br.edu.ifrs.restinga.market.mymarket.controller;

import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductResponseDTO;

import java.util.Optional;
import java.util.List;

public interface ProductController {

    List<ProductResponseDTO> findAll(Optional<String> name, Optional<String> type);

    ProductResponseDTO create(ProductRequestDTO request);

    void delete(String id);

    ProductResponseDTO update(String id, Optional<Double> value, Optional<Integer> quantity);
}
