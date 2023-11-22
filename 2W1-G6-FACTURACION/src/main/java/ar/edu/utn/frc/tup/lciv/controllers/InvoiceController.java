package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.*;
import ar.edu.utn.frc.tup.lciv.dtos.common.ErrorApi;
import ar.edu.utn.frc.tup.lciv.services.InvoiceService;
import ar.edu.utn.frc.tup.lciv.services.PdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import java.util.List;
import java.util.Objects;

@RestController
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PdfService pdfService;

    @Operation(
            summary = "Register a new invoice on the system",
            description = "Return the invoice created with the creation date and ID and client CUIT associated")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation, invoice created",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payments sum is minor than invoice totalAmount",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            )
    })
    @PostMapping("api/v1/invoice")
    public ResponseEntity<InvoiceCreatedResponse> CreateInvoice(@RequestBody CreateInvoiceRequest data) throws Exception {
        InvoiceCreatedResponse invoiceCreatedResponse = invoiceService.CreateInvoice(data);
        if(Objects.isNull(invoiceCreatedResponse)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payments sum is minor than invoice totalAmount");
        }
        return ResponseEntity.ok(invoiceCreatedResponse);
    }

    @Operation(
            summary = "Find an invoice by id value",
            description = "Return the invoice by the id, if doesn't exists one returns 404")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            )
    })
    @GetMapping("api/v1/invoice/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long id){
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @Operation(
            summary = "Find all the invoices",
            description = "Return the invoices registered on the system, if there isn't any invoice returns 404")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            )
    })
    @GetMapping("api/v1/invoice/all")
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices(){
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }


    @Operation(
            summary = "Find all the details related to and invoice, by invoice id value",
            description = "Return the Details by the by the invoiceId, if the invoice with id specified doesn't exists returns 404")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            )
    })
    @GetMapping("api/v1/invoice/details/{id}")
    public ResponseEntity<List<InvoiceDetailDto>> getInvoiceDetails(@PathVariable Long id){
        return ResponseEntity.ok(invoiceService.getInvoiceDetails(id));
    }

    @Operation(
            summary = "Find all the invoices according to the filters sent",
            description = "Return the invoices registered on the system just by the filters indicated, is not necessary to fill them all you can just apply some" +
                    " , if there isn't any invoice matching filters returns 404")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            )
    })
    @GetMapping("api/v1/invoice/all/filtered")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByFilter(
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "clientId", required = false) Long clientId) {

        List<InvoiceDTO> filteredInvoices = invoiceService.getInvoicesByFilter(dateFrom, dateTo, clientId);
        return ResponseEntity.ok(filteredInvoices);
    }

    @Operation(
            summary = "Find total amount billed in a date range",
            description = "search the total amount billed by two filters dateto and datefrom you can fill bought, use just one or search by them all " +
                    " , if there isn't any invoice matching filters returns 404")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorApi.class)
                    )
            )
    })
    @GetMapping("api/v1/invoice/amount/by-date")
    public ResponseEntity<BigDecimal> getBilledAmountByClientAndDate(
            @RequestParam(value = "dateFrom", required = false) String dateFrom,
            @RequestParam(value = "dateTo", required = false) String dateTo,
            @RequestParam(value = "clientId", required=false) Long clientId){

        BigDecimal totalBilled = invoiceService.getBilledAmountByDateAndClient(dateFrom, dateTo, clientId);
        if(Objects.isNull(totalBilled)){
            throw new EntityNotFoundException("No invoices found on date range");
        }
        else
            return ResponseEntity.ok(totalBilled);
    }

    @PutMapping("api/v1/invoice/delete/{id}")
    public  ResponseEntity<InvoiceDTO> updateInvoiceStat(@PathVariable Long id){
        return  ResponseEntity.ok(invoiceService.updateState(id));
    }

    @GetMapping("api/v1/invoice/generate-pdf-bytes/{id}")
    public ResponseEntity<ByteArrayResource> generateInvoicePdfBytes(@PathVariable Long id) {
        byte[] pdfBytes = pdfService.generateInvoicePdfBytes(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice_" + id + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdfBytes));
    }


}
