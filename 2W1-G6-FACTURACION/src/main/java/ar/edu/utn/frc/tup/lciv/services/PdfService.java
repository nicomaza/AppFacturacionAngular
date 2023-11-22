package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.models.reports.ClientsReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PdfService {
     byte[] generateInvoicePdfBytes(Long invoiceId);
      byte[] generateReportByClientPdf(List<ClientsReport> clientsReports );
}
