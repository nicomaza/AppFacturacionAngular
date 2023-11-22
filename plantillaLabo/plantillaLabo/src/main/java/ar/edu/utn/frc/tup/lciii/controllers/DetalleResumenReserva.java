package ar.edu.utn.frc.tup.lciii.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleResumenReserva {
    private String Producto;
    private double cantidad;
}
