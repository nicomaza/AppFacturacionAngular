package ar.edu.utn.frc.tup.lciv.services;


import ar.edu.utn.frc.tup.lciv.dtos.*;
import ar.edu.utn.frc.tup.lciv.entities.InvoiceEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public interface InvoiceService {
    InvoiceCreatedResponse CreateInvoice(CreateInvoiceRequest data) throws Exception;
    InvoiceDTO getInvoiceById(Long id);

    List<InvoiceDetailDto> getInvoiceDetails(Long invoiceId);
    List<InvoiceDTO> getAllInvoices();
    List<InvoiceDTO> getInvoicesByFilter(String dateFrom, String dateTo, Long clientId);
    InvoiceDTO updateState(Long id);

    BigDecimal getBilledAmountByDateAndClient(String dateFrom, String dateTo, Long clientId);
    InvoiceEntity getInvoiceEntityById(Long id);

}
