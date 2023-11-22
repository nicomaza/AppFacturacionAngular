package ar.edu.utn.frc.tup.lciv.services.Impl;

import ar.edu.utn.frc.tup.lciv.clients.moduleClient.ModuleClientRestClient;
import ar.edu.utn.frc.tup.lciv.clients.models.*;
import ar.edu.utn.frc.tup.lciv.clients.moduleClient.models.ModuleClientDTO;
import ar.edu.utn.frc.tup.lciv.clients.moduleInventory.ModuleInventoryRestClient;
import ar.edu.utn.frc.tup.lciv.clients.moduleSales.ModuleSalesRestClient;
import ar.edu.utn.frc.tup.lciv.dtos.*;
import ar.edu.utn.frc.tup.lciv.dtos.clients.ClientDto;
import ar.edu.utn.frc.tup.lciv.entities.*;
import ar.edu.utn.frc.tup.lciv.models.*;
import ar.edu.utn.frc.tup.lciv.models.enums.StatusEnum;
import ar.edu.utn.frc.tup.lciv.models.enums.StatusOrderEnum;
import ar.edu.utn.frc.tup.lciv.repositories.InvoiceDetailEntityJpaRepository;
import ar.edu.utn.frc.tup.lciv.repositories.InvoiceEntityJpaRepository;
import ar.edu.utn.frc.tup.lciv.services.InvoiceService;
import ar.edu.utn.frc.tup.lciv.services.PaymentService;
import ar.edu.utn.frc.tup.lciv.utils.DateHelper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import javax.swing.text.html.Option;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private InvoiceEntityJpaRepository invoiceEntityJpaRepository;
    @Autowired
    private InvoiceDetailEntityJpaRepository invoiceDetailEntityJpaRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ModuleClientRestClient clientRestClient;
    @Autowired
    private ModuleInventoryRestClient moduleInventoryRestClient;
    @Autowired
    private ModuleSalesRestClient moduleSalesRestClient;


    @Value("${empresa.cuit}")
    private String cuit;
    @Value("${empresa.razonsocial}")
    private String razonSocial;

    DateHelper dateHelper = new DateHelper();

    @Override
    @Transactional
    public InvoiceCreatedResponse CreateInvoice(CreateInvoiceRequest data) throws Exception {
        InvoiceEntity invoice = new InvoiceEntity();
        // Create principal invoice object
        invoice.setDate(new Date());
        invoice.setCuit(cuit);
        invoice.setRazonSocial(razonSocial);
        invoice.setNroPedido(data.getOrderId());
        invoice.setStatus(data.getStatus().toString());
        invoice.setType(data.getType().name());

        if(data.getClientId() == null){
            invoice.setClientId(1L);
        }else{
            invoice.setClientId(data.getClientId());
        }

        // Calculate total amount
        invoice.setTotalAmount(calculateTotalAmount(data.getDetails(),data.getDiscountRequestList()));

        if (data.getType().toString().equals("A")) {
            invoice.setIva(data.getIva());
            discriminateIva(invoice);
        }
        //set invoice payment methods
        setPaymentMethods(invoice, data.getPaymentMethodList());

        //set invoice details
        setInvoiceDetails(invoice, data.getDetails());

        //set discounts
        if(data.getDiscountRequestList() != null)
            setInvoiceDiscounts(invoice,data.getDiscountRequestList());

        InvoiceEntity savedInvoice = invoiceEntityJpaRepository.save(invoice);
        if (Objects.isNull(savedInvoice)) {
            return null;
        }

        moduleInventoryRestClient.confirmarReserva(data.getReservationId());
        moduleSalesRestClient.actualizarEstadoOrden(data.getOrderId(), 2L);

        // Crear y devolver la respuesta

        InvoiceCreatedResponse invoiceCreatedResponse = new InvoiceCreatedResponse();
        invoiceCreatedResponse.setId(savedInvoice.getId());
        invoiceCreatedResponse.setDate(savedInvoice.getDate().toString());
        invoiceCreatedResponse.setCuit(savedInvoice.getCuit());
        return invoiceCreatedResponse;
    }

    @Override
    public InvoiceDTO getInvoiceById(Long invoiceId) {
        // Utiliza el método findById del repositorio de InvoiceEntity para buscar la factura por su ID
        Optional<InvoiceEntity> optionalInvoice = invoiceEntityJpaRepository.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            InvoiceEntity invoiceEntity = optionalInvoice.get();
            InvoiceDTO invoice = modelMapper.map(invoiceEntity, InvoiceDTO.class);
            ModuleClientDTO moduleClientDTO = clientRestClient.getClientByNroDoc(invoiceEntity.getClientId()).getBody();
            if(!Objects.isNull(moduleClientDTO)) {
                invoice.setClient(moduleClientDTO.getNombre());
                invoice.setClientType(moduleClientDTO.getId_categoria_fiscal().getDescripcion());
            }
            else{
                invoice.setClient("Not Found");
                invoice.setClientType("X");
            }
            return invoice;
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<InvoiceDetailDto> getInvoiceDetails(Long invoiceId) {
        Optional<InvoiceEntity> optionalInvoice = invoiceEntityJpaRepository.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            List<InvoiceDetailDto> detailDtos = new ArrayList<>();
            List<InvoiceDetailEntity> invoiceDetailEntities = optionalInvoice.get().getDetails();
            for (InvoiceDetailEntity detail : invoiceDetailEntities
            ) {
                detailDtos.add(modelMapper.map(detail, InvoiceDetailDto.class));
            }
            return detailDtos;
        } else
            return null;
    }
  
    @Override
    public List<InvoiceDTO> getAllInvoices() {
        List<InvoiceEntity> invoiceEntities = invoiceEntityJpaRepository.findAll();
        return getInvoiceDTOSWithClient(invoiceEntities);
    }

    @Override
    public List<InvoiceDTO> getInvoicesByFilter(String dateFrom, String dateTo, Long client_nro_doc) {
        Date parsedDateFrom = null;
        Date parsedDateTo = null;
        if(!Objects.isNull(dateFrom)) {
             parsedDateFrom = dateHelper.parseDate(dateFrom);
        }
        if(!Objects.isNull(dateTo)) {
             parsedDateTo = dateHelper.parseDate(dateTo);
        }
        List<InvoiceEntity> invoiceEntities = invoiceEntityJpaRepository.getInvoicesByFilter(parsedDateFrom, parsedDateTo, client_nro_doc);
        if(!Objects.isNull(invoiceEntities) && invoiceEntities.size()>0) {
           return getInvoiceDTOSWithClient(invoiceEntities);
        }
        else
            return null;
    }

    @Override
    public InvoiceDTO updateState(Long id) {
        InvoiceEntity invoice = invoiceEntityJpaRepository.getReferenceById(id);
        invoice.setStatus(StatusEnum.CANCELED.toString());
        invoiceEntityJpaRepository.save(invoice);

        InvoiceDTO invoiceDT = new InvoiceDTO();
        invoiceDT.setId(invoice.getId());
        invoiceDT.setDate(invoice.getDate().toString());
        invoiceDT.setStatus(invoice.getStatus());
        invoiceDT.setTotalAmount(invoice.getTotalAmount());

        return invoiceDT;
    }

    @Override
    public BigDecimal getBilledAmountByDateAndClient(String dateFrom, String dateTo, Long clientId) {

        Date parsedDateFrom = null;
        Date parsedDateTo = null;
        if(!Objects.isNull(dateFrom)) {
            parsedDateFrom = dateHelper.parseDate(dateFrom);
        }
        if(!Objects.isNull(dateTo)) {
            parsedDateTo = dateHelper.parseDate(dateTo);
        }
        List<InvoiceEntity> invoicesEntities = invoiceEntityJpaRepository.getInvoicesByFilter( parsedDateFrom,  parsedDateTo, clientId);
        if(!Objects.isNull(invoicesEntities) && invoicesEntities.size()>0) {
            List<InvoiceDTO> invoices = getInvoiceDTOS(invoicesEntities);
            BigDecimal total = BigDecimal.ZERO;
            for (InvoiceDTO invoice : invoices
            ) {
                total = total.add(invoice.getTotalAmount());
            }
            return total;
        }
        else
            return null;
    }

    @Override
    public InvoiceEntity getInvoiceEntityById(Long id) {
        Optional<InvoiceEntity> invoiceEntity = invoiceEntityJpaRepository.findById(id);
        return invoiceEntity.orElse(null);
    }


    private List<InvoiceDTO> getInvoiceDTOSWithClient(List<InvoiceEntity> invoiceEntities) {
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        for (InvoiceEntity invoiceEntity : invoiceEntities) {
            InvoiceDTO invoiceDTO = modelMapper.map(invoiceEntity, InvoiceDTO.class);
            ModuleClientDTO moduleClientDTO = clientRestClient.getClientByNroDoc(invoiceEntity.getClientId()).getBody();
            if(!Objects.isNull(moduleClientDTO)) {
                invoiceDTO.setClient(moduleClientDTO.getNombre() + " " + moduleClientDTO.getApellido());
                invoiceDTO.setClientType(moduleClientDTO.getId_categoria_fiscal().getDescripcion());
            }else{
                invoiceDTO.setClient("Not Found");
            }
            invoiceDTOS.add(invoiceDTO);
        }
        if(invoiceDTOS.size()>0)
            return invoiceDTOS;
        else
            return null;
    }

    private List<InvoiceDTO> getInvoiceDTOS(List<InvoiceEntity> invoiceEntities) {
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        for (InvoiceEntity invoiceEntity : invoiceEntities) {
            InvoiceDTO invoiceDTO = modelMapper.map(invoiceEntity, InvoiceDTO.class);
            invoiceDTOS.add(invoiceDTO);
        }
        if(invoiceDTOS.size()>0)
            return invoiceDTOS;
        else
            return null;
    }


    private void setPaymentMethods(InvoiceEntity invoice, List<PaymentsRequest> paymentMethodList) throws Exception {
        List<InvoicePaymentMethodEntity> paymentMethodEntities = new ArrayList<>();
        for (PaymentsRequest payment : paymentMethodList
        ) {
            PaymentMethod paymentMethod = paymentService.GetById(payment.getPaymentMethod());
            PaymentMethodEntity paymentMethodEntity = modelMapper.map(paymentMethod, PaymentMethodEntity.class);

            InvoicePaymentMethodEntity invoicePaymentMethodEntity = new InvoicePaymentMethodEntity();
            invoicePaymentMethodEntity.setPaymentMethod(paymentMethodEntity);
            invoicePaymentMethodEntity.setAmount(payment.getAmount());
            invoicePaymentMethodEntity.setInvoice(invoice);

            paymentMethodEntities.add(invoicePaymentMethodEntity);
        }
        if (!checkAmountReached(invoice.getTotalAmount(), paymentMethodEntities)) {
            throw new Exception("Payments amount are different of invoice amount");
        } else
            invoice.setPaymentMethodEntities(paymentMethodEntities);
    }

    private void setInvoiceDetails(InvoiceEntity invoice, List<DetailDTO> detailDTOList) {
        List<InvoiceDetailEntity> invoiceDetails = new ArrayList<>();
        for (DetailDTO detail : detailDTOList) {
            InvoiceDetailEntity invoiceDetail = new InvoiceDetailEntity();
            invoiceDetail.setProductId(detail.getProduct().getProduct_id());
            invoiceDetail.setCount(detail.getAmount());
            invoiceDetail.setPrice(detail.getProduct().getPrice());
            invoiceDetail.setMeasurementUnit(detail.getMeasurementUnit());
            invoiceDetail.setProduct(detail.getProduct().getName());
            invoiceDetail.setInvoice(invoice);

            invoiceDetails.add(invoiceDetail);
        }
        invoice.setDetails(invoiceDetails);
    }

    private void setInvoiceDiscounts(InvoiceEntity invoice, List<DiscountRequest> discountRequestList){
        List<DiscountEntity> discountEntities = new ArrayList<>();
        for(DiscountRequest dr : discountRequestList){
            DiscountEntity discountEntity = new DiscountEntity();
            discountEntity.setAmount(dr.getMonto());
            discountEntity.setDescription(dr.getDescription());

            discountEntities.add(discountEntity);
        }
        invoice.setDiscoutns(discountEntities);
    }

    private void discriminateIva(InvoiceEntity invoice) {
        BigDecimal totalAmount = invoice.getTotalAmount();
        BigDecimal ivaPercentage = invoice.getIva();
        BigDecimal ivaAmount = totalAmount.multiply(ivaPercentage);
        BigDecimal totalAmountWithoutIva = totalAmount.subtract(ivaAmount);
        invoice.setIvaAmount(ivaAmount);
        invoice.setTotalAmountWithoutIva(totalAmountWithoutIva);
    }

    private boolean checkAmountReached(BigDecimal total, List<InvoicePaymentMethodEntity> paymentMethods) {
        BigDecimal totalPayments = BigDecimal.ZERO;
        for (InvoicePaymentMethodEntity paymentMethod : paymentMethods
        ) {
            totalPayments = totalPayments.add(paymentMethod.getAmount());
        }
        return totalPayments.compareTo(total) == 0;
    }

    private BigDecimal calculateTotalAmount(List<DetailDTO> invoiceDetails, List<DiscountRequest> discountRequestList) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetailDTO detail : invoiceDetails) {
            BigDecimal totalDetail = detail.getProduct().getPrice().multiply(BigDecimal.valueOf(detail.getAmount()));
            total = total.add(totalDetail);
        }
        if(discountRequestList != null){
            for (DiscountRequest discount : discountRequestList){
                total = total.subtract(discount.getMonto());
            }
        }

        total = total.setScale(2, BigDecimal.ROUND_DOWN);
        return total;
    }



}
