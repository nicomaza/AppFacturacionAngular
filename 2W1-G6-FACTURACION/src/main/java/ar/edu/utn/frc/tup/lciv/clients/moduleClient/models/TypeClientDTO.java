package ar.edu.utn.frc.tup.lciv.clients.moduleClient.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeClientDTO {

    private Long id_tipo_cliente;
    private String tipo_cliente;
}
