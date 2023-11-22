package ar.edu.utn.frc.tup.lciv.dtos;

import ar.edu.utn.frc.tup.lciv.models.enums.TypeEnum;
import ar.edu.utn.frc.tup.lciv.models.PaymentsRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class newCreateInvoiceRequest {
    private Long orderId;
    private TypeEnum type;
    private List<PaymentsRequest> paymentMethodList;
}
