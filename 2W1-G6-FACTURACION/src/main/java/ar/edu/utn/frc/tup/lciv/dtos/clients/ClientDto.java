package ar.edu.utn.frc.tup.lciv.dtos.clients;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private Long nro_doc;
    private String name;
    private String lastName;
    private String clientType;
}
