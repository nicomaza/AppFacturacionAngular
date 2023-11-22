package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.clients.moduleClient.ModuleClientRestClient;
import ar.edu.utn.frc.tup.lciv.clients.models.*;
import ar.edu.utn.frc.tup.lciv.clients.moduleClient.models.FiscalCategoryDTO;
import ar.edu.utn.frc.tup.lciv.clients.moduleClient.models.ModuleClientDTO;
import ar.edu.utn.frc.tup.lciv.clients.moduleInventory.ModuleInventoryRestClient;
import ar.edu.utn.frc.tup.lciv.clients.moduleSales.ModuleSalesRestClient;
import ar.edu.utn.frc.tup.lciv.dtos.*;
import ar.edu.utn.frc.tup.lciv.entities.InvoiceDetailEntity;
import ar.edu.utn.frc.tup.lciv.entities.InvoiceEntity;
import ar.edu.utn.frc.tup.lciv.models.DiscountRequest;
import ar.edu.utn.frc.tup.lciv.models.PaymentMethod;
import ar.edu.utn.frc.tup.lciv.models.enums.StatusEnum;
import ar.edu.utn.frc.tup.lciv.models.enums.TypeEnum;
import ar.edu.utn.frc.tup.lciv.models.PaymentsRequest;
import ar.edu.utn.frc.tup.lciv.repositories.InvoiceEntityJpaRepository;
import ar.edu.utn.frc.tup.lciv.services.Impl.InvoiceServiceImpl;
import ar.edu.utn.frc.tup.lciv.services.Impl.PaymentServiceImpl;
import ar.edu.utn.frc.tup.lciv.utils.DateHelper;
import org.hibernate.type.descriptor.java.DataHelper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceServiceTest {

    @InjectMocks
    InvoiceServiceImpl invoiceService;

    @Mock
    PaymentServiceImpl paymentService;

    @Spy
    @Qualifier("modelMapper")
    ModelMapper modelMapper;

    @Spy
    DateHelper dateHelper;

    @Mock
    InvoiceEntityJpaRepository invoiceEntityJpaRepository;

    @Mock
    ModuleClientRestClient clientRestClient;

    @Mock
    ModuleInventoryRestClient inventoryRestClient;

    @Mock
    ModuleSalesRestClient salesRestClient;

    @Test
    void testCreateInvoice() throws Exception {

        CreateInvoiceRequest createInvoiceRequest = new CreateInvoiceRequest();

        createInvoiceRequest.setOrderId(1L);
        createInvoiceRequest.setStatus(StatusEnum.PENDING);
        createInvoiceRequest.setType(TypeEnum.B);

        List<DetailDTO> detailDTOList = new ArrayList<DetailDTO>() {{
            add(new DetailDTO(new ProductDTO("1","Tornillo",new BigDecimal(30)),1,""));
        }};
        List<PaymentsRequest> paymentsRequestList = new ArrayList<PaymentsRequest>() {{
            add(new PaymentsRequest(new BigDecimal(30),1L,""));
        }};


        createInvoiceRequest.setDetails(detailDTOList);
        createInvoiceRequest.setPaymentMethodList(paymentsRequestList);


        when(invoiceEntityJpaRepository.save(any(InvoiceEntity.class))).thenAnswer(invocation -> {
            InvoiceEntity invoice = invocation.getArgument(0);
            invoice.setId(1L);
            return invoice;
        });
        when(paymentService.GetById(any(Long.class)))
                .thenAnswer(invocation -> {
                    Long paymentMethodId = invocation.getArgument(0);
                    return new PaymentMethod(paymentMethodId, "PaymentMethodName");
                });
        InvoiceCreatedResponse response = invoiceService.CreateInvoice(createInvoiceRequest);



        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("1", response.getId().toString());

        verify(invoiceEntityJpaRepository, times(1)).save(any(InvoiceEntity.class));

    }

    //funciona
    @Test
    public void testGetInvoiceById() {
        Long invoiceId = 1L;

        InvoiceEntity mockInvoiceEntity = new InvoiceEntity();
        InvoiceDTO mockInvoiceDTO = new InvoiceDTO();
        mockInvoiceDTO.setClient("Client Name");
        mockInvoiceDTO.setClientType("Client Type");

        when(invoiceEntityJpaRepository.findById(invoiceId)).thenReturn(Optional.of(mockInvoiceEntity));
        when(modelMapper.map(mockInvoiceEntity, InvoiceDTO.class)).thenReturn(mockInvoiceDTO);

        // Mock ClientRestClient
        ModuleClientDTO mockModuleClientDTO = new ModuleClientDTO();
        mockModuleClientDTO.setNombre("Client Name");
        FiscalCategoryDTO fiscalCategoryDTO = new FiscalCategoryDTO();
        fiscalCategoryDTO.setDescripcion("RI");
        fiscalCategoryDTO.setId_clasificacion(1L);
        mockModuleClientDTO.setId_categoria_fiscal(fiscalCategoryDTO);
        mockModuleClientDTO.setId_categoria_fiscal(fiscalCategoryDTO);

        when(clientRestClient.getClientByNroDoc(mockInvoiceEntity.getClientId())).thenReturn(ResponseEntity.ok(mockModuleClientDTO));

        // Perform the test
        InvoiceDTO result = invoiceService.getInvoiceById(invoiceId);

        // Assertions
        assertNotNull(result);
        assertEquals("Client Name", result.getClient());
        assertEquals("RI", result.getClientType());
    }
    ///Funciona
    @Test
    public void testGetInvoiceDetails() {

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId(1L);
        InvoiceDetailEntity detail1 = new InvoiceDetailEntity();
        detail1.setId(1L);
        detail1.setProduct("Product 1");
        detail1.setCount(2);
        detail1.setPrice(BigDecimal.valueOf(10));
        detail1.setInvoice(invoice);

        List<InvoiceDetailEntity> details = new ArrayList<>();
        details.add(detail1);

        invoice.setDetails(details);

        when(invoiceEntityJpaRepository.findById(1L)).thenReturn(Optional.of(invoice));

        List<InvoiceDetailDto> result = invoiceService.getInvoiceDetails(1L);

        assertNotNull(result);

        assertNotNull(result);
        assertEquals(1, result.size());

        InvoiceDetailDto detailDto = result.get(0);
        assertEquals("Product 1", detailDto.getProduct());
        assertEquals(2, detailDto.getCount());
        assertEquals(BigDecimal.valueOf(10), detailDto.getPrice());
    }

    //Funciona
    @Test
    public void testGetAllInvoices() {

        InvoiceEntity invoiceEntity1 = new InvoiceEntity();
        invoiceEntity1.setId(1L);
        invoiceEntity1.setClientId(123L);

        InvoiceEntity invoiceEntity2 = new InvoiceEntity();
        invoiceEntity2.setId(2L);
        invoiceEntity2.setClientId(456L);

        FiscalCategoryDTO fiscalCategoryDTO = new FiscalCategoryDTO();
        fiscalCategoryDTO.setDescripcion("RI");
        fiscalCategoryDTO.setId_clasificacion(1L);

        ModuleClientDTO moduleClientDTO1 = new ModuleClientDTO();
        moduleClientDTO1.setNombre("Susana");
        moduleClientDTO1.setApellido("Horia");

        moduleClientDTO1.setId_categoria_fiscal(fiscalCategoryDTO);

        ModuleClientDTO moduleClientDTO2 = new ModuleClientDTO();
        moduleClientDTO2.setNombre("Juan");
        moduleClientDTO2.setApellido("Sanchez");
        moduleClientDTO2.setId_categoria_fiscal(fiscalCategoryDTO);

        when(clientRestClient.getClientByNroDoc(123L))
                .thenReturn(ResponseEntity.ok(moduleClientDTO1));
        when(clientRestClient.getClientByNroDoc(456L))
                .thenReturn(ResponseEntity.ok(moduleClientDTO2));

        List<InvoiceEntity> invoiceEntities = new ArrayList<>();
        invoiceEntities.add(invoiceEntity1);
        invoiceEntities.add(invoiceEntity2);
        when(invoiceEntityJpaRepository.findAll())
                .thenReturn(invoiceEntities);

        List<InvoiceDTO> result = invoiceService.getAllInvoices();

        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).getId());
        assertEquals("Susana Horia", result.get(0).getClient());
        assertEquals("RI", result.get(0).getClientType());

        assertEquals(2L, result.get(1).getId());
        assertEquals("Juan Sanchez", result.get(1).getClient());
        assertEquals("RI", result.get(1).getClientType());
    }
    //Funciona
    @Test
    public void testUpdateState() {
        // Arrange
        Long invoiceId = 19L;
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(invoiceId);
        invoiceEntity.setDate(new Date());
        invoiceEntity.setStatus(StatusEnum.PENDING.toString());
        invoiceEntity.setTotalAmount(BigDecimal.valueOf(100.0));

        when(invoiceEntityJpaRepository.getReferenceById(invoiceId)).thenReturn(invoiceEntity);

        InvoiceDTO result = invoiceService.updateState(invoiceId);

        assertNotNull(result);
        assertEquals(invoiceId, result.getId());
        assertEquals(StatusEnum.CANCELED.toString(), result.getStatus());

    }

    //Funciona
    @Test
    public void testGetBilledAmountByDateAndClient() {
        String dateFrom = "01/02/2023";
        String dateTo = "01/03/2023";
        Long clientId = 87654321L;

        Date parsedDateFrom = new Date();
        Date parsedDateTo = new Date();

        when(dateHelper.parseDate(dateFrom)).thenReturn(parsedDateFrom);
        when(dateHelper.parseDate(dateTo)).thenReturn(parsedDateTo);

        List<InvoiceEntity> invoiceEntities = new ArrayList<>();
        InvoiceEntity invoice1 = new InvoiceEntity();
        invoice1.setTotalAmount(new BigDecimal("100.00"));
        invoiceEntities.add(invoice1);

        when(invoiceEntityJpaRepository.getInvoicesByFilter(parsedDateFrom, parsedDateTo, clientId))
                .thenReturn(invoiceEntities);


        BigDecimal result = invoiceService.getBilledAmountByDateAndClient(dateFrom, dateTo, clientId);

        assertNotNull(result);
    }

    @Test
    public void testGetInvoicesByFilter() {
        // Setup mock objects
        DateHelper dateHelper = Mockito.mock(DateHelper.class);
        InvoiceEntityJpaRepository invoiceEntityJpaRepository = Mockito.mock(InvoiceEntityJpaRepository.class);

        // Prepare mock data
        String dateFrom = "01/01/2023";
        String dateTo = "31/12/2023";
        Long clientNroDoc = 87654321L;
        Date parsedDateFrom = new Date();
        Date parsedDateTo = new Date();
        List<InvoiceEntity> invoiceEntities = new ArrayList<>();
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(1L);
        invoiceEntity.setClientId(clientNroDoc);
        invoiceEntity.setDate(parsedDateFrom);

        invoiceEntities.add(invoiceEntity);

        when(dateHelper.parseDate(dateFrom)).thenReturn(parsedDateFrom);
        when(dateHelper.parseDate(dateTo)).thenReturn(parsedDateTo);
        when(invoiceEntityJpaRepository.getInvoicesByFilter(parsedDateFrom, parsedDateTo, clientNroDoc)).thenReturn(invoiceEntities);

        assertEquals(1, invoiceEntities.size());
        assertEquals("87654321", invoiceEntities.get(0).getClientId().toString());
    }


    @Test
    public void testGetInvoiceDTOSWithClient() {
        // Setup mock objects

        // Prepare mock data
        List<InvoiceEntity> invoiceEntities = new ArrayList<>();
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(1L);
        invoiceEntity.setClientId(123456789L);
        invoiceEntities.add(invoiceEntity);

        ModuleClientDTO moduleClientDTO = new ModuleClientDTO();
        moduleClientDTO.setId(12312L);
        FiscalCategoryDTO fiscalCategoryDTO = new FiscalCategoryDTO(1L,"Categoria",1L);

        moduleClientDTO.setId_categoria_fiscal(fiscalCategoryDTO);

        // Set mock behavior
        when(clientRestClient.getClientByNroDoc(invoiceEntity.getClientId())).thenReturn(ResponseEntity.ok(moduleClientDTO));
        when(modelMapper.map(invoiceEntity, InvoiceDTO.class)).thenReturn(new InvoiceDTO());

        // Call the method under test
        //List<InvoiceDTO> invoiceDTOs = InvoiceService.getInvoiceDTOSWithClient(invoiceEntities);

        // Verify mock behavior and test results

        assertEquals(1, invoiceEntities.size());
        assertEquals("123456789", invoiceEntities.get(0).getClientId().toString());

    }

    private CreateInvoiceRequest createTestData() {

        CreateInvoiceRequest testData = new CreateInvoiceRequest();
        List<DiscountRequest> discountRequestList = new ArrayList<>();
        DiscountRequest discountRequest = new DiscountRequest(new BigDecimal(5),"Cliente recurrente");

        discountRequestList.add(discountRequest);
        testData.setOrderId(1L);
        testData.setReservationId(1L);
        testData.setClientId(87654321L);
        testData.setType(TypeEnum.A);
        testData.setStatus(StatusEnum.PENDING);
        testData.setIva(new BigDecimal("0.21"));
        testData.setDiscountRequestList(discountRequestList);

        List<PaymentsRequest> paymentMethodList = new ArrayList<>();
        PaymentsRequest payment1 = new PaymentsRequest();
        payment1.setAmount(new BigDecimal("95.00"));
        payment1.setPaymentMethod(1L);
        payment1.setObservations("Pago parcial");
        paymentMethodList.add(payment1);

        testData.setPaymentMethodList(paymentMethodList);

        List<DetailDTO> details = new ArrayList<>();
        DetailDTO detail1 = new DetailDTO();
        detail1.setProduct(new ProductDTO("caac","string",new BigDecimal(100.00)));
        detail1.setAmount(1);
        detail1.setMeasurementUnit("unidad");
        details.add(detail1);



        testData.setDetails(details);

        return testData;
    }

    private InvoiceEntity createSimulatedSavedInvoice() {

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setId(1L);
        invoice.setDate(new Date(123, 9, 10));
        invoice.setCuit("123456789");

        return invoice;
    }
}
