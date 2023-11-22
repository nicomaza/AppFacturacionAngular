package ar.edu.utn.frc.tup.lciv.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePaymentMethod {
    private Long id;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private String observations;
}
