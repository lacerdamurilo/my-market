package br.edu.ifrs.restinga.market.mymarket.service;

import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductResponseDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductUpdateRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductResponseDTO> findAll(Optional<String> name, Optional<String> type);

    Product findById(String id);

    ProductResponseDTO create(ProductRequestDTO request);

    void delete(String id);

    ProductResponseDTO update(String id, ProductUpdateRequestDTO request);
}
