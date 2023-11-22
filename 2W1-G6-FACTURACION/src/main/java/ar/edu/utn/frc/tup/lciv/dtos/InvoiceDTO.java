package ar.edu.utn.frc.tup.lciv.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
    private Long id;
    private String client;
    private Long clientId;
    private String status;
    private String date;
    private String type;
    private BigDecimal totalAmount;
    private String clientType;
}
