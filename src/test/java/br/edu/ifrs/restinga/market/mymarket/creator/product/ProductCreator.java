package br.edu.ifrs.restinga.market.mymarket.creator.product;

import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductResponseDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductUpdateRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.entity.Product;
import br.edu.ifrs.restinga.market.mymarket.util.mapper.ProductMapper;
import com.google.gson.Gson;

public class ProductCreator {

    private static Gson GSON = new Gson();

    public static final ProductRequestDTO PRODUCT_REQUEST_DTO_1 = ProductRequestDTO.builder()
            .name("Star Fruit")
            .type("Fruit")
            .value(5.99)
            .quantity(100)
            .build();

    public static final ProductRequestDTO PRODUCT_REQUEST_DTO_2 = ProductRequestDTO.builder()
            .name("Watermelon")
            .type("Fruit")
            .value(15.0)
            .quantity(90)
            .build();

    public static final ProductRequestDTO PRODUCT_REQUEST_DTO_3 = ProductRequestDTO.builder()
            .name("Watermelon")
            .type("Fruit")
            .value(-20.0)
            .quantity(90)
            .build();

    public static final ProductUpdateRequestDTO PRODUCT_UPDATE_REQUEST_DTO_4 = ProductUpdateRequestDTO.builder()
            .value(10.0)
            .quantity(150)
            .build();

    public static final ProductUpdateRequestDTO PRODUCT_UPDATE_REQUEST_DTO_5 = ProductUpdateRequestDTO.builder()
            .value(30.0)
            .quantity(200)
            .build();

    public static final Product PRODUCT_1 = ProductMapper.fromRequest(PRODUCT_REQUEST_DTO_1).toBuilder()
            .id("1")
            .build();

    public static final Product PRODUCT_2 = ProductMapper.fromRequest(PRODUCT_REQUEST_DTO_2).toBuilder()
            .id("2")
            .build();

    public static final ProductResponseDTO PRODUCT_RESPONSE_DTO_1 = ProductMapper.fromEntity(PRODUCT_1);

    public static final ProductResponseDTO PRODUCT_RESPONSE_DTO_2 = ProductMapper.fromEntity(PRODUCT_2);

    public static final String PRODUCT_REQUEST_JSON_1 = GSON.toJson(PRODUCT_REQUEST_DTO_1);

    public static final String PRODUCT_REQUEST_JSON_2 = GSON.toJson(
            PRODUCT_REQUEST_DTO_1.toBuilder().value(PRODUCT_2.getValue()).quantity(PRODUCT_2.getQuantity()).build()
    );

    public static final String PRODUCT_REQUEST_JSON_3 = GSON.toJson(
            PRODUCT_REQUEST_DTO_1.toBuilder().value(null).quantity(null).build()
    );

}
