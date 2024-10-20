package br.edu.ifrs.restinga.market.mymarket.controller.impl;

import br.edu.ifrs.restinga.market.mymarket.controller.ProductController;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductResponseDTO;
import br.edu.ifrs.restinga.market.mymarket.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Products", description = "Product management system Johnson")
@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    @Override
    @GetMapping("find")
    @Operation(summary = "List products")
    public List<ProductResponseDTO> findAll(@RequestParam(required = false) Optional<String> name,
                                            @RequestParam(required = false) Optional<String> type) {
        return productService.findAll(name, type);
    }

    @Override
    @PostMapping("create")
    @Operation(summary = "Register products - Login required", security = @SecurityRequirement(name = "basicAuth"))
    public ProductResponseDTO create(ProductRequestDTO request) {
        return productService.create(request);
    }

    @Override
    @DeleteMapping("delete/{id}")
    @Operation(summary = "Delete products - Login required", security = @SecurityRequirement(name = "basicAuth"))
    public void delete(@PathVariable(name = "id") String id) {
        productService.delete(id);
    }

    @Override
    @PutMapping("update/{id}")
    @Operation(summary = "Update products value and quantity - Login required", security = @SecurityRequirement(name = "basicAuth"))
    public ProductResponseDTO update(@PathVariable(name = "id") String id,
                                     @RequestParam(required = false) Optional<Double> value,
                                     @RequestParam(required = false) Optional<Integer> quantity) {
        return productService.update(id, value, quantity);
    }
}
