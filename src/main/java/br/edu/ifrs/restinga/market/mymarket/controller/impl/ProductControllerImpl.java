package br.edu.ifrs.restinga.market.mymarket.controller.impl;

import br.edu.ifrs.restinga.market.mymarket.controller.ProductController;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductResponseDTO;
import br.edu.ifrs.restinga.market.mymarket.model.dto.ProductUpdateRequestDTO;
import br.edu.ifrs.restinga.market.mymarket.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Tag(name = "Products", description = "Product management system Johnson")
@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    @Override
    @GetMapping("find")
    @ResponseStatus(OK)
    @Operation(summary = "List products")
    public List<ProductResponseDTO> findAll(@RequestParam(required = false) Optional<String> name,
                                            @RequestParam(required = false) Optional<String> type) {
        return productService.findAll(name, type);
    }

    @Override
    @PostMapping("create")
    @ResponseStatus(CREATED)
    @Operation(summary = "Register products - Login required", security = @SecurityRequirement(name = "basicAuth"))
    public ProductResponseDTO create(@Valid @RequestBody ProductRequestDTO request) {
        return productService.create(request);
    }

    @Override
    @DeleteMapping("delete/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete products - Login required", security = @SecurityRequirement(name = "basicAuth"))
    public void delete(@PathVariable(name = "id") String id) {
        productService.delete(id);
    }

    @Override
    @PutMapping("update/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Update products value and quantity - Login required", security = @SecurityRequirement(name = "basicAuth"))
    public ProductResponseDTO update(@PathVariable(name = "id") String id,
                                     @Valid @RequestBody ProductUpdateRequestDTO request) {
        return productService.update(id, request);
    }
}
