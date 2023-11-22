package ar.edu.utn.frc.tup.lciv.clients.moduleInventory;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ModuleInventoryRestClientIT {
    //Todos Funcionan
    @Autowired
    private ModuleInventoryRestClient moduleInventoryRestClient;

    @Test
    void cancelarReservaTest() {
        Long id = 12345L;
        ResponseEntity<?> response = moduleInventoryRestClient.cancelarReserva(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void confirmarReservaTest() {
        Long id = 67890L;

        ResponseEntity<?> response = moduleInventoryRestClient.confirmarReserva(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
