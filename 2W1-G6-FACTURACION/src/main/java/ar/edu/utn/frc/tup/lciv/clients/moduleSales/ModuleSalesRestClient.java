package ar.edu.utn.frc.tup.lciv.clients.moduleSales;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ModuleSalesRestClient {
    RestTemplate restTemplate = new RestTemplate();
    String urlConfirm = "http://localhost:8082/Orden/";

    public ResponseEntity<Boolean> actualizarEstadoOrden(Long id, Long state) {
        String url = urlConfirm + id + "?state=" + state;

        HttpEntity<String> requestEntity = new HttpEntity<>(null);

        ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Boolean.class);

        return response;
    }

}
