package ar.edu.utn.frc.tup.lciv.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetailDto {
    private String product;
    private Integer count;
    private BigDecimal price;
    private String measurementUnit;
    private BigDecimal subTotal;
}
