package br.edu.ifrs.restinga.market.mymarket.util.mapper;

import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductResponseDTO;
import br.edu.ifrs.restinga.market.mymarket.model.entity.Product;

public class ProductMapper {

    public static Product fromRequest(ProductRequestDTO request) {
        return Product.builder()
                .name(request.getName())
                .type(request.getType())
                .value(request.getValue())
                .quantity(request.getQuantity())
                .build();
    }

    public static ProductResponseDTO fromEntity(Product entity) {
        return ProductResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .value(entity.getValue())
                .quantity(entity.getQuantity())
                .build();
    }
}
