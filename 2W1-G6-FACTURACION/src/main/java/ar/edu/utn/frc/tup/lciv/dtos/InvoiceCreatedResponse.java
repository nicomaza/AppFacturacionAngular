package ar.edu.utn.frc.tup.lciv.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCreatedResponse {
    private Long id;
    private String date;
    private String cuit;
}
