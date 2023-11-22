package ar.edu.utn.frc.tup.lciv.models;

import ar.edu.utn.frc.tup.lciv.models.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private Long id;

    private Long clientId;

    private String client;

    private String date;

    private String cuit;

    private String razonSocial;

    private Long nroPedido;

    private StatusEnum status;

    private String type;

    private BigDecimal totalAmount;

    private BigDecimal iva;

    private BigDecimal ivaAmount;

    private BigDecimal totalAmountWithoutIva;

    private List<InvoicePaymentMethod> paymentMethods;

    private List<InvoiceDetail> details;


}
