package ar.edu.utn.frc.tup.lciii.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResumenResponce {
    private Long id;
    private List<DetalleResumenReserva> stockReservado;
}
