package ar.edu.utn.frc.tup.lciii.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@RestController
public class InventoriModule {

    @PutMapping("Inventario/reservas/cancelar/{id}")
    public ResponseEntity<ReservaResumenResponce> cancelarReserva(@PathVariable Long id){
        List<DetalleResumenReserva> resumenReservaList = new ArrayList<>();
        resumenReservaList.add(new DetalleResumenReserva( "Cajon", 2));
        resumenReservaList.add(new DetalleResumenReserva("Papel",10));
        ReservaResumenResponce reservaResumenResponce = new ReservaResumenResponce(1L,resumenReservaList);

        return ResponseEntity.ok(reservaResumenResponce);
    }

    @PutMapping("Inventario/reservas/confirmar/{id}")
    public ResponseEntity<ReservaResumenResponce> confirmarReserva(@PathVariable Long id){
        List<DetalleResumenReserva> resumenReservaList = new ArrayList<>();
        resumenReservaList.add(new DetalleResumenReserva( "Cajon", 2));
        resumenReservaList.add(new DetalleResumenReserva("Papel",10));
        ReservaResumenResponce reservaResumenResponce = new ReservaResumenResponce(1L,resumenReservaList);

        return ResponseEntity.ok(reservaResumenResponce);
    }

}
