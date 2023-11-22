package ar.edu.utn.frc.tup.lciv.clients.moduleClient;

import ar.edu.utn.frc.tup.lciv.clients.moduleClient.models.ModuleClientDTO;
import ar.edu.utn.frc.tup.lciv.dtos.clients.ClientDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ModuleClientRestClient {
    RestTemplate restTemplate = new RestTemplate();
    String baseUrlResourse = "https://my-json-server.typicode.com/113974-Olivera-Gustavo/api-clients-bd/clientes";

    public ResponseEntity<ModuleClientDTO> getClientByNroDoc(Long nro_doc) {
        ResponseEntity<ModuleClientDTO[]> responseEntity = restTemplate.getForEntity(baseUrlResourse + "?nro_doc=" + nro_doc, ModuleClientDTO[].class);
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.hasBody()) {
            ModuleClientDTO[] moduleClientDTOS = responseEntity.getBody();
            if (moduleClientDTOS != null && moduleClientDTOS.length > 0) {
                return ResponseEntity.ok(moduleClientDTOS[0]);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
