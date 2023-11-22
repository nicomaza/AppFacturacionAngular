package ar.edu.utn.frc.tup.lciv.clients.moduleClient.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleClientDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String domicilio;
    private Long nro_doc;
    private TypeDocumentDTO id_tipo_doc;
    private FiscalCategoryDTO id_categoria_fiscal;
    private TypeClientDTO id_tipo_cliente;
    private int cantPuntos;
}
