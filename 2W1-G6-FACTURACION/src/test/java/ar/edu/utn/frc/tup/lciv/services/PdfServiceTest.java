package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.InvoiceDTO;
import ar.edu.utn.frc.tup.lciv.dtos.InvoiceDetailDto;

import ar.edu.utn.frc.tup.lciv.models.reports.ClientsReport;
import ar.edu.utn.frc.tup.lciv.services.Impl.PdfServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PdfServiceTest {
    @InjectMocks
    private PdfServiceImpl pdfService;

    @Mock
    private InvoiceService invoiceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateInvoicePdfBytes() {
        // Datos de prueba
        Long invoiceId = 1L;
        InvoiceDTO mockInvoice = new InvoiceDTO();
        mockInvoice.setId(invoiceId);
        mockInvoice.setClient("TestClient");
        mockInvoice.setClientId(101L);
        mockInvoice.setStatus("Paid");
        mockInvoice.setDate("2023-01-01");
        mockInvoice.setType("Sale");
        mockInvoice.setTotalAmount(new BigDecimal("100.00"));
        mockInvoice.setClientType("Regular");

        List<InvoiceDetailDto> mockDetails = Arrays.asList(
                new InvoiceDetailDto("Item1", 2, new BigDecimal("50.00"), "Unit1",new BigDecimal("50.00")),
                new InvoiceDetailDto("Item2", 3, new BigDecimal("25.00"), "Unit2",new BigDecimal("50.00"))
        );

        // Configura el comportamiento simulado del servicio de facturas
        when(invoiceService.getInvoiceById(invoiceId)).thenReturn(mockInvoice);
        when(invoiceService.getInvoiceDetails(invoiceId)).thenReturn(mockDetails);

        // Llama al método que estás probando
        byte[] result = pdfService.generateInvoicePdfBytes(invoiceId);

        // Verifica que el servicio de facturas haya sido llamado con el ID de la factura
        verify(invoiceService).getInvoiceById(invoiceId);

        // Verifica que el servicio de facturas haya sido llamado con el ID de la factura para obtener los detalles
        verify(invoiceService).getInvoiceDetails(invoiceId);

        // Por ejemplo, podrías verificar que el resultado no sea nulo
        assertNotNull(result);

    }

}
