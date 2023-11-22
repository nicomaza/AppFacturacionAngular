package ar.edu.utn.frc.tup.lciv.clients.moduleClient.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiscalCategoryDTO {

    private Long id_monotributo;
    private String descripcion;
    private Long id_clasificacion;
}
