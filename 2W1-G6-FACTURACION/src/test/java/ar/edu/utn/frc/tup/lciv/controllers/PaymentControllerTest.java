package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.models.PaymentMethod;
import ar.edu.utn.frc.tup.lciv.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    //Funciona
    @Test
    public void getByIdTest() throws Exception {
        mockMvc.perform(get("http://localhost:8081/api/v1/paymentMethods/{id}", 1L))
                .andExpect(status().isOk());
        Mockito.verify(paymentService, Mockito.times(1)).GetById(1L);
    }
    //Funciona
    @Test
    public void getAllTest() throws Exception {
        mockMvc.perform(get("http://localhost:8081/api/v1/paymentMethods"))
                .andExpect(status().isOk());
        Mockito.verify(paymentService, Mockito.times(1)).GetPaymentMethods();
    }
    //Funciona
    @Test
    public void createTest() throws Exception {
        String paymentMethodName = "QR";
        PaymentMethod createdMethod = new PaymentMethod();
        createdMethod.setId(4L);
        createdMethod.setName(paymentMethodName);

        Mockito.when(paymentService.CreatePaymentMethod(paymentMethodName)).thenReturn(createdMethod);

        mockMvc.perform(post("http://localhost:8081/api/v1/paymentMethods/{name}", paymentMethodName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value(paymentMethodName));

        Mockito.verify(paymentService, Mockito.times(1)).CreatePaymentMethod(paymentMethodName);
    }

    //Funciona
    @Test
    public void deleteByIdTest() throws Exception {
        Long paymentMethodId = 1L;
        boolean deletionResult = true;

        Mockito.when(paymentService.DeletePaymentMethod(paymentMethodId)).thenReturn(deletionResult);

        mockMvc.perform(delete("http://localhost:8081/api/v1/paymentMethod/delete/{id}", paymentMethodId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        Mockito.verify(paymentService, Mockito.times(1)).DeletePaymentMethod(paymentMethodId);
    }
    //Funciona
    @Test
    public void updateTest() throws Exception {
        PaymentMethod updatedMethod = new PaymentMethod();
        updatedMethod.setId(1L);
        updatedMethod.setName("TarjetaActualizada");

        Mockito.when(paymentService.UpdatePaymentMethod(updatedMethod)).thenReturn(updatedMethod);

        ObjectMapper objectMapper = new ObjectMapper();
        String updatedMethodJson = objectMapper.writeValueAsString(updatedMethod);

        mockMvc.perform(put("http://localhost:8081/api/v1/updatePaymentMethod")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMethodJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TarjetaActualizada"));

        Mockito.verify(paymentService, Mockito.times(1)).UpdatePaymentMethod(updatedMethod);
    }

}
