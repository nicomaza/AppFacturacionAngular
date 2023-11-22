package ar.edu.utn.frc.tup.lciv.dtos.models.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypePaymentDto {
    private String formapago;
    private BigDecimal monto;
}
