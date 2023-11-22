package ar.edu.utn.frc.tup.lciv.clients.moduleInventory;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ModuleInventoryRestClient {

    RestTemplate restTemplate = new RestTemplate();

    String baseURL = "http://localhost:8082/";

    public ResponseEntity<?> cancelarReserva(Long id) {
        String url = baseURL + "Inventario/reservas/cancelar/" + id;
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.PUT, null, Object.class);
        return response;
    }

    public ResponseEntity<?> confirmarReserva(Long id) {
        String url = baseURL + "Inventario/reservas/confirmar/" + id;
        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.PUT, null, Object.class);
        return response;
    }
}
