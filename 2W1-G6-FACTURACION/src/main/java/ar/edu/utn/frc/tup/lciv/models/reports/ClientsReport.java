package ar.edu.utn.frc.tup.lciv.models.reports;

import ar.edu.utn.frc.tup.lciv.clients.moduleClient.models.FiscalCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientsReport {

    private String name;
    private String lastName;
    private Long nro_doc;
    private String phone;
    private String clientType;
    private BigDecimal totalAmount;
}
