package ar.edu.utn.frc.tup.lciv.dtos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private Long id;
    private Long id_client;
    private Date date;
    private BigInteger cuit;
}
