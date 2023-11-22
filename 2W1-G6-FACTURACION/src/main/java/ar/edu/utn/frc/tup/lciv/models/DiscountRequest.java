package ar.edu.utn.frc.tup.lciv.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {

    private BigDecimal monto;
    private String description;
}
