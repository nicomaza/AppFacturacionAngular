package ar.edu.utn.frc.tup.lciv.clients.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailDTO {

    private ProductDTO product;
    private Integer amount;
    private String measurementUnit;

}
