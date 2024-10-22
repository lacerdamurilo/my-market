package br.edu.ifrs.restinga.market.mymarket.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String name;
    @NotBlank
    @Size(max = 100)
    private String type;
    @NotNull
    @Positive
    private double value;
    @NotNull
    @PositiveOrZero
    private int quantity;

}
