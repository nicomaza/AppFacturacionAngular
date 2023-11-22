package ar.edu.utn.frc.tup.lciv.clients.moduleClient.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeDocumentDTO {

    private Long id_tipo_doc;
    private String  tipo_documento;
}
