package ar.edu.utn.frc.tup.lciii.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SalesModule {

    @PutMapping("Orden/{id}")
    public ResponseEntity<Boolean> estadoOrden(@PathVariable Long id, @RequestParam Long state){
                return ResponseEntity.ok(true);
    }


}
