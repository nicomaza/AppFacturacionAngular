package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.BilledMonthDto;
import ar.edu.utn.frc.tup.lciv.dtos.models.reports.TypePaymentDto;
import ar.edu.utn.frc.tup.lciv.models.reports.ClientsReport;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ReportsService {

    List<ClientsReport> getTotalAmountByClients(String dateFrom, String dateTo);

    List<TypePaymentDto> getTotalAmountByType(String dateFrom, String dateTo);
     byte[] generateReportByClientPdf(String dateFrom, String dateTo);

    List<BilledMonthDto> getMonthlyTotalAmount(String dateFrom, String dateTo);

}
