package ar.edu.utn.frc.tup.lciv.clients.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String product_id;
    private String name;
    private BigDecimal price;

}
