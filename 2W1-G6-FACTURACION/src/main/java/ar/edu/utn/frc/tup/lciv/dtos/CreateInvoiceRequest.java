package ar.edu.utn.frc.tup.lciv.dtos;

import ar.edu.utn.frc.tup.lciv.clients.models.DetailDTO;
import ar.edu.utn.frc.tup.lciv.models.DiscountRequest;
import ar.edu.utn.frc.tup.lciv.models.enums.StatusEnum;
import ar.edu.utn.frc.tup.lciv.models.enums.TypeEnum;
import ar.edu.utn.frc.tup.lciv.models.PaymentsRequest;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInvoiceRequest {
    private Long orderId;
    private Long clientId;
    private TypeEnum type;
    private StatusEnum status;
    private BigDecimal iva;
    @Nullable
    private List<DiscountRequest> discountRequestList;
    private Long reservationId;
    private List<PaymentsRequest> paymentMethodList;
    private List<DetailDTO> details;
}
