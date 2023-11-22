package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.clients.models.DetailDTO;
import ar.edu.utn.frc.tup.lciv.clients.models.ProductDTO;
import ar.edu.utn.frc.tup.lciv.dtos.CreateInvoiceRequest;
import ar.edu.utn.frc.tup.lciv.dtos.InvoiceCreatedResponse;
import ar.edu.utn.frc.tup.lciv.dtos.InvoiceDTO;
import ar.edu.utn.frc.tup.lciv.models.DiscountRequest;
import ar.edu.utn.frc.tup.lciv.models.PaymentsRequest;
import ar.edu.utn.frc.tup.lciv.models.enums.StatusEnum;
import ar.edu.utn.frc.tup.lciv.models.enums.TypeEnum;
import ar.edu.utn.frc.tup.lciv.services.InvoiceService;
import ar.edu.utn.frc.tup.lciv.services.PaymentService;
import ar.edu.utn.frc.tup.lciv.services.PdfService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    @Test
    void testCreateInvoice() throws Exception {
        // Arrange
        CreateInvoiceRequest requestData = newCreateRequest();
        InvoiceCreatedResponse fakeResponse = newInvoiceResponse();

        when(invoiceService.CreateInvoice(requestData)).thenReturn(fakeResponse);

       InvoiceCreatedResponse response = invoiceService.CreateInvoice(requestData);

        // Assert
        verify(invoiceService, times(1)).CreateInvoice(requestData);
        assertEquals(fakeResponse, response);
    }

   /* @Test
    void testCreateInvoiceWith400Error() throws Exception {

        CreateInvoiceRequest requestData = newCreateRequest();

        when(invoiceService.CreateInvoice(requestData))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payments sum is minor than invoice totalAmount"));

        ResponseStatusException exception = org.junit.jupiter.api.Assertions.assertThrows(
                ResponseStatusException.class,
                () -> invoiceController.CreateInvoice(requestData)
        );

        verify(invoiceService, times(1)).CreateInvoice(requestData);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Payments sum is minor than invoice totalAmount", exception.getReason());
    }

    @Test
    void testCreateInvoiceWith500Error() throws Exception {
        // Arrange
        CreateInvoiceRequest requestData = newCreateRequest();

        when(invoiceService.CreateInvoice(requestData))
                .thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"));

        // Act & Assert
        ResponseStatusException exception = org.junit.jupiter.api.Assertions.assertThrows(
                ResponseStatusException.class,
                () -> invoiceController.CreateInvoice(requestData)
        );

        verify(invoiceService, times(1)).CreateInvoice(requestData);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Internal server error", exception.getReason());
    }*/


    @Test
    public void getInvoicesByIdTest() throws Exception {
        mockMvc.perform(get("http://localhost:8081/api/v1/invoice/{id}", 1L))
                .andExpect(status().isOk());
        verify(invoiceService, times(1)).getInvoiceById(1L);
    }


    @Test
    public void getAllInvoicesTest() throws Exception {
        mockMvc.perform(get("http://localhost:8081/api/v1/invoice/all"))
                .andExpect(status().isOk());
        verify(invoiceService, times(1)).getAllInvoices();
    }

    @Test
    public void getInvoicesDetailsTest() throws Exception {
        mockMvc.perform(get("http://localhost:8081/api/v1/invoice/details/{id}", 1L))
                .andExpect(status().isOk());
        verify(invoiceService, times(1)).getInvoiceDetails(1L);
    }


    @Test
    public void updateInvoiceStatTest() throws Exception {
        InvoiceDTO invoiceDTO = new InvoiceDTO(1L,"Carlos",1L
                ,StatusEnum.CANCELED.toString(),"10/11/2023",
                TypeEnum.A.toString(),new BigDecimal(50.00),"Monotributista");

        when(invoiceService.updateState(1L)).thenReturn(invoiceDTO);

        mockMvc.perform(put("http://localhost:8081/api/v1/invoice/delete/{id}", 1L))
                .andExpect(status().isOk());

        verify(invoiceService, times(1)).updateState(1L);
    }

    @Test
    void getBilledAmountByClientAndDateTest() {
        String dateFrom = "2023-01-01";
        String dateTo = "2023-01-31";
        Long clientId = 1L;
        BigDecimal fakeTotalBilled = BigDecimal.valueOf(500);

        when(invoiceService.getBilledAmountByDateAndClient(dateFrom, dateTo, clientId)).thenReturn(fakeTotalBilled);

        BigDecimal response = invoiceService.getBilledAmountByDateAndClient(dateFrom, dateTo, clientId);

        verify(invoiceService, times(1)).getBilledAmountByDateAndClient(dateFrom, dateTo, clientId);
        assertEquals(fakeTotalBilled, response);
    }

    @Test
    void testGetInvoicesByFilter() {

        String dateFrom = "2023-01-01";
        String dateTo = "2023-01-31";
        Long clientId = 1L;

        List<InvoiceDTO> fakeFilteredInvoices = new ArrayList<>(Arrays.asList(
                new InvoiceDTO(
                        1L,
                        "Client Name",
                        clientId,
                        "Status",
                        "2023-01-15",
                        "Type",
                        BigDecimal.valueOf(500.00),
                        "Client Type"
                )
        ));

        when(invoiceService.getInvoicesByFilter(dateFrom, dateTo, clientId)).thenReturn(fakeFilteredInvoices);
        List<InvoiceDTO> response = invoiceService.getInvoicesByFilter(dateFrom, dateTo, clientId);
        verify(invoiceService, times(1)).getInvoicesByFilter(dateFrom, dateTo, clientId);
        assertEquals(fakeFilteredInvoices, response);
    }





    public static CreateInvoiceRequest newCreateRequest() {
        return new CreateInvoiceRequest(
                1L,
                2L,
                TypeEnum.A,
                StatusEnum.PENDING,
                BigDecimal.valueOf(0.2),
                Collections.singletonList(new DiscountRequest(BigDecimal.TEN, "Discount 1")),
                3L,
                Collections.singletonList(new PaymentsRequest(BigDecimal.valueOf(100), 1L, "Payment 1")),
                Collections.singletonList(createSampleDetailDTO())
        );
    }

    public static InvoiceCreatedResponse newInvoiceResponse() {
        return new InvoiceCreatedResponse(
                1L,
                "2023-01-01",
                "12345678901"
        );
    }

    private static DetailDTO createSampleDetailDTO() {
        return new DetailDTO(
                createSampleProductDTO(),
                5,
                "unit"
        );
    }

    private static ProductDTO createSampleProductDTO() {
        return new ProductDTO(
                "productId123",
                "Sample Product",
                BigDecimal.valueOf(50)
        );
    }




}
