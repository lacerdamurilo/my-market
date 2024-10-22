package br.edu.ifrs.restinga.market.mymarket.model.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequestDTO {

    @Positive
    private Double value;
    @PositiveOrZero
    private Integer quantity;

}
