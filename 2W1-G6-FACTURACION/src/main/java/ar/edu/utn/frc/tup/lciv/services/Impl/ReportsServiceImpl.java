package ar.edu.utn.frc.tup.lciv.services.Impl;

import ar.edu.utn.frc.tup.lciv.clients.moduleClient.ModuleClientRestClient;
import ar.edu.utn.frc.tup.lciv.clients.moduleClient.models.ModuleClientDTO;
import ar.edu.utn.frc.tup.lciv.dtos.BilledMonthDto;
import ar.edu.utn.frc.tup.lciv.dtos.models.reports.TypePaymentDto;
import ar.edu.utn.frc.tup.lciv.entities.InvoiceEntity;
import ar.edu.utn.frc.tup.lciv.models.reports.ClientsReport;
import ar.edu.utn.frc.tup.lciv.repositories.ReportsJpaRepository;
import ar.edu.utn.frc.tup.lciv.services.PdfService;
import ar.edu.utn.frc.tup.lciv.services.ReportsService;
import ar.edu.utn.frc.tup.lciv.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReportsServiceImpl implements ReportsService {

    @Autowired
    private ReportsJpaRepository reportsJpaRepository;

    @Autowired
    private ModuleClientRestClient moduleClientRestClient;
    @Autowired
    private PdfService pdfService;

    @Override
    public List<ClientsReport> getTotalAmountByClients(String dateFrom, String dateTo) {
        Date parsedDateFrom = null;
        Date parsedDateTo = null;
        DateHelper dateHelper = new DateHelper();

        if(!Objects.isNull(dateFrom)) {
            parsedDateFrom = dateHelper.parseDate(dateFrom);
        }
        if(!Objects.isNull(dateTo)) {
            parsedDateTo = dateHelper.parseDate(dateTo);
        }

        ClientsReport clientsReport = new ClientsReport();
        List<ClientsReport> listClientsReports = new ArrayList<>();
        List<Object[]> listInvoiceEntity = reportsJpaRepository.getInvoicesByDateRange(parsedDateFrom,parsedDateTo);

        for(Object[] i : listInvoiceEntity){
            Long clientId = (Long) i[0]; // Accedes al clientId
            BigDecimal montoTotal = (BigDecimal) i[1];
            ModuleClientDTO moduleClientDTO = moduleClientRestClient.getClientByNroDoc(clientId).getBody();
            if(!Objects.isNull(moduleClientDTO)) {
                clientsReport.setName(moduleClientDTO.getNombre());
                clientsReport.setLastName(moduleClientDTO.getApellido());
                clientsReport.setNro_doc(moduleClientDTO.getNro_doc());
                clientsReport.setPhone(moduleClientDTO.getTelefono());
                clientsReport.setClientType(moduleClientDTO.getId_categoria_fiscal().getDescripcion());
            }
            else{
                clientsReport.setName("Consumidor");
                clientsReport.setLastName(" Final");
                clientsReport.setNro_doc(0L);
                clientsReport.setPhone("0");
                clientsReport.setClientType("CF");
            }
            clientsReport.setTotalAmount(montoTotal);
            listClientsReports.add(clientsReport);
            clientsReport = new ClientsReport();
        }
        return listClientsReports;
    }

    @Override
    public List<TypePaymentDto> getTotalAmountByType(String dateFrom, String dateTo) {
        Date parsedDateFrom = null;
        Date parsedDateTo = null;
        DateHelper dateHelper = new DateHelper();

        if(!Objects.isNull(dateFrom)) {
            parsedDateFrom = dateHelper.parseDate(dateFrom);
        }
        if(!Objects.isNull(dateTo)) {
            parsedDateTo = dateHelper.parseDate(dateTo);
        }
        List<TypePaymentDto> clientsReport = new ArrayList<>();
        List<Object[]> listPayFromType = reportsJpaRepository.getTotalAmountByPaymentMethod(parsedDateFrom,parsedDateTo);
        for(Object[] i : listPayFromType){

            TypePaymentDto typePaymentDto = new TypePaymentDto();

            String formaPago = (String) i[0];
            BigDecimal monto = (BigDecimal) i[1];

            typePaymentDto.setFormapago(formaPago);
            typePaymentDto.setMonto(monto);


            clientsReport.add(typePaymentDto);

        }
        return clientsReport;

    }

    @Override
    public byte[] generateReportByClientPdf(String dateFrom, String dateTo) {
        List<ClientsReport> clientsReports = getTotalAmountByClients(dateFrom,dateTo);
        return pdfService.generateReportByClientPdf(clientsReports);
    }

    @Override
    public List<BilledMonthDto> getMonthlyTotalAmount(String dateFrom, String dateTo) {
        DateHelper dateHelper = new DateHelper();
        Date parsedDateFrom = null;
        Date parsedDateTo = null;
        if(!Objects.isNull(dateFrom)) {
            parsedDateFrom = dateHelper.parseDate(dateFrom);
        }
        if(!Objects.isNull(dateTo)) {
            parsedDateTo = dateHelper.parseDate(dateTo);
        }
        List<Object[]> result = reportsJpaRepository.getMonthlyTotalAmount(parsedDateFrom, parsedDateTo);

        return result.stream()
                .map(obj -> new BilledMonthDto(
                        String.valueOf(obj[0]), // Mes
                        new BigDecimal(String.valueOf(obj[1])) // TotalAmount
                ))
                .collect(Collectors.toList());
    }
}
