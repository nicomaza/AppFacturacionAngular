package ar.edu.utn.frc.tup.lciv.clients.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    public int status;
    private List<DetailDTO> detailDTO;
}
