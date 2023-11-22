package ar.edu.utn.frc.tup.lciv.clients.moduleClient;

import ar.edu.utn.frc.tup.lciv.clients.moduleClient.models.ModuleClientDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ModuleClientRestClientIT {

    @Autowired
    private ModuleClientRestClient moduleClientRestClient;
    //Funciona
    @Test
    void getClientByNroDoc(){
        ResponseEntity<ModuleClientDTO> result = moduleClientRestClient.getClientByNroDoc(87654321L);
        assertEquals("Susana", Objects.requireNonNull(result.getBody().getNombre()));
    }
    //Funciona
    @Test
    void getClientByNroDoc2() {
        ResponseEntity<ModuleClientDTO> result = moduleClientRestClient.getClientByNroDoc(87654321L);
        ModuleClientDTO moduleClientDTO = result.getBody();

        assertNotNull(moduleClientDTO);
        assertEquals(1, moduleClientDTO.getId());
        assertEquals("Susana", moduleClientDTO.getNombre());
        assertEquals("Horia", moduleClientDTO.getApellido());


       }
}
