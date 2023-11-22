package ar.edu.utn.frc.tup.lciv.clients.moduleSales;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ModuleSalesRestClientIT {

    @Autowired
    private ModuleSalesRestClient moduleSalesRestClient;

    @Test
    void actualizarEstadoOrdenTest() {
        Long id = 12L;
        Long state = 2L;

        ResponseEntity<Boolean> response = moduleSalesRestClient.actualizarEstadoOrden(id,state);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody());
    }
}
