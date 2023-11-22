package ar.edu.utn.frc.tup.lciv.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetail {
    private Long id;
    private String productId;
    private Integer count;
    private BigDecimal price;
    private String measurementUnit;
}
