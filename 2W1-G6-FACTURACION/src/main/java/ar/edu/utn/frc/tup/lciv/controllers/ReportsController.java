package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.BilledMonthDto;
import ar.edu.utn.frc.tup.lciv.dtos.models.reports.TypePaymentDto;
import ar.edu.utn.frc.tup.lciv.models.reports.ClientsReport;
import ar.edu.utn.frc.tup.lciv.services.PdfService;
import ar.edu.utn.frc.tup.lciv.services.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class ReportsController {

    @Autowired
    private ReportsService reportsService;


    @GetMapping("/ReportsByClients")
    public ResponseEntity<List<ClientsReport>> getById(@RequestParam String dateFrom, @RequestParam String dateTo) {
        return ResponseEntity.ok(reportsService.getTotalAmountByClients(dateFrom,dateTo));
    }
    @GetMapping("/reportByPayement")
    public ResponseEntity<List<TypePaymentDto>> getListPaymentReport(@RequestParam String dateFrom, @RequestParam String dateTo) {
        return ResponseEntity.ok(reportsService.getTotalAmountByType(dateFrom,dateTo));
    }

    @GetMapping("/reportsByClients/pdf")
    public ResponseEntity<ByteArrayResource> generateReportByClientPdf(@RequestParam String dateFrom, @RequestParam String dateTo) {
        byte[] pdfBytes = reportsService.generateReportByClientPdf( dateFrom, dateTo);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report_clients_from" + dateFrom + "_to_" + dateTo +".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdfBytes));
    }

    @GetMapping("api/v1/invoice/billed-months")
    public ResponseEntity<List<BilledMonthDto>> getMonthlyTotalAmount(
            @RequestParam String dateFrom, @RequestParam String dateTo
    ) {
        List<BilledMonthDto> billedMonths = reportsService.getMonthlyTotalAmount(dateFrom, dateTo);
        return ResponseEntity.ok(billedMonths);
    }

}
