package ar.edu.utn.frc.tup.lciv.services.Impl;

import ar.edu.utn.frc.tup.lciv.clients.models.DetailDTO;
import ar.edu.utn.frc.tup.lciv.dtos.InvoiceDTO;
import ar.edu.utn.frc.tup.lciv.dtos.InvoiceDetailDto;
import ar.edu.utn.frc.tup.lciv.entities.InvoiceDetailEntity;
import ar.edu.utn.frc.tup.lciv.entities.InvoiceEntity;
import ar.edu.utn.frc.tup.lciv.models.Invoice;
import ar.edu.utn.frc.tup.lciv.models.reports.ClientsReport;
import ar.edu.utn.frc.tup.lciv.services.InvoiceService;
import ar.edu.utn.frc.tup.lciv.services.PdfService;
import ar.edu.utn.frc.tup.lciv.services.ReportsService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    InvoiceService invoiceService;


    public byte[] generateInvoicePdfBytes(Long invoiceId) {
        try {
            //first create a base Document to star making edits on it
            Document document = new Document();
            //get the invoice to fill info on the document
            InvoiceDTO invoice = invoiceService.getInvoiceById(invoiceId);
            List<InvoiceDetailDto> details = invoiceService.getInvoiceDetails(invoiceId);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            addDocumentLogo(document);
            setMasterInvoiceInfo(document, invoice);
            setDetailsTable(document, details,invoice);
            document.close();

            return byteArrayOutputStream.toByteArray();
        } catch (DocumentException | IOException e) {
            e.printStackTrace(); // Manejar adecuadamente las excepciones
            return new byte[0];
        }
    }

    @Override
    public byte[] generateReportByClientPdf(List<ClientsReport>  clientsReports ) {
        try {
            //first create a base Document to star making edits on it
            Document document = new Document();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            addDocumentLogo(document);
            document.add(createClientsTable(clientsReports));
            document.close();
            return byteArrayOutputStream.toByteArray();
        }
        catch (DocumentException | IOException e) {
            e.printStackTrace(); // Manejar adecuadamente las excepciones
            return new byte[0];
        }
    }

    //Invoice
    private void setMasterInvoiceInfo(Document document, InvoiceDTO invoice) throws DocumentException {
        PdfPTable table = new PdfPTable(2);

        //set space before last element on the document to separate it
        Paragraph paragraph = new Paragraph();
        paragraph.setSpacingBefore(30);
        document.add(paragraph);

        //Add the invoice type on a single cell column
        PdfPTable tableType  = new PdfPTable(1);
        tableType.setWidthPercentage(20);
        tableType.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cellInvoiceType = new PdfPCell();
        cellInvoiceType.setBackgroundColor(new BaseColor(0xE0, 0xE0, 0xE0));
        cellInvoiceType.setBorderWidth(2);
        Font font = new Font(Font.FontFamily.HELVETICA, 40, Font.BOLD);
        Paragraph paragraphCell = new Paragraph(invoice.getType(), font);
        paragraphCell.setAlignment(Element.ALIGN_CENTER);
        cellInvoiceType.addElement(paragraphCell);
        tableType.addCell(cellInvoiceType);
        document.add(tableType);

        // Client
        PdfPCell cellClientLabel = new PdfPCell(new Phrase("Cliente:"));
        cellClientLabel.setBackgroundColor(new BaseColor(0xE0, 0xE0, 0xE0));
        cellClientLabel.setBorderWidth(2);
        PdfPCell cellClientData = new PdfPCell(new Phrase(invoice.getClient()));
        table.addCell(cellClientLabel);
        table.addCell(cellClientData);

        // Date
        PdfPCell cellDateLabel = new PdfPCell(new Phrase("Fecha:"));
        cellDateLabel.setBackgroundColor(new BaseColor(0xE0, 0xE0, 0xE0));
        cellDateLabel.setBorderWidth(2);
        PdfPCell cellDateData = new PdfPCell(new Phrase(invoice.getDate()));
        table.addCell(cellDateLabel);
        table.addCell(cellDateData);

        // Client Type
        PdfPCell cellTypeLabel = new PdfPCell(new Phrase("Tipo cliente:"));
        cellTypeLabel.setBackgroundColor(new BaseColor(0xE0, 0xE0, 0xE0));
        cellTypeLabel.setBorderWidth(2);
        PdfPCell cellTypeData = new PdfPCell(new Phrase(invoice.getClientType()));
        table.addCell(cellTypeLabel);
        table.addCell(cellTypeData);

        // Align and table space
        table.setWidthPercentage(100);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        document.add(table);
    }

    private void setDetailsTable(Document document, List<InvoiceDetailDto> invoiceDetailsEntities, InvoiceDTO invoiceDTO) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        //list of table headers
        List<String> detailsColumns = Arrays.asList(
                "Producto",
                "Cantidad",
                "U. De medida",
                "Precio",
                "SubTotal"
        );
        table.setWidthPercentage(100);
        addTableHeader(table, detailsColumns);
        addRows(table, invoiceDetailsEntities, invoiceDTO);
        document.add(table);
        addTotalAmount(document, invoiceDTO.getTotalAmount());
    }

    private void addTotalAmount(Document document, BigDecimal total) throws DocumentException {
        //add a table on bottom of the last table showing total amount
        PdfPTable tableTotal = new PdfPTable(2);
        tableTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cellTotal = new PdfPCell(new Phrase("Total:"));
        cellTotal.setBackgroundColor(new BaseColor(0xE0, 0xE0, 0xE0));
        tableTotal.addCell(cellTotal);
        tableTotal.addCell(total.toString());
        tableTotal.setWidthPercentage(40);
        document.add(tableTotal);
    }
    private void addTableHeader(PdfPTable table, List<String> tableHeaders) {
        tableHeaders.forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(1);
            header.setPhrase(new Phrase(new Chunk(columnTitle)));
            table.addCell(header);
        });
    }

    private void addDocumentLogo(Document document) throws DocumentException, IOException {
        //search for the document on local path and set it on top center position
        Image logo = Image.getInstance("logo.jpg");
        logo.setAlignment(Element.ALIGN_TOP);
        logo.setAlignment(Element.ALIGN_CENTER);
        logo.scalePercent(35);
        document.add(logo);
        Paragraph paragraph = new Paragraph();
        paragraph.setSpacingAfter(30);
        document.add(paragraph);
    }

    private void addRows(PdfPTable table, List<InvoiceDetailDto> invoiceDetailsEntities, InvoiceDTO invoice) {
        //add details invoice rows
        for (int i = 0; i<invoiceDetailsEntities.size(); i++){
            BigDecimal detailSubtotal = getDetailSubtotal(invoiceDetailsEntities.get(i));
            table.addCell(invoiceDetailsEntities.get(i).getProduct());
            table.addCell(invoiceDetailsEntities.get(i).getCount().toString());
            table.addCell(invoiceDetailsEntities.get(i).getMeasurementUnit());
            table.addCell(invoiceDetailsEntities.get(i).getPrice().toString());
            table.addCell(detailSubtotal.toString());
        }

    }

    private BigDecimal getDetailSubtotal(InvoiceDetailDto invoiceDetail){
        if (invoiceDetail.getCount() != null && invoiceDetail.getPrice() != null) {
            int count = invoiceDetail.getCount();
            BigDecimal price = invoiceDetail.getPrice();

            BigDecimal subtotal = BigDecimal.valueOf(count).multiply(price);

            return subtotal;
        } else {
            return BigDecimal.ZERO;
        }
    }

    //Report
    private PdfPTable createClientsTable(List<ClientsReport> clientsReports){
        PdfPTable pdfPTable = new PdfPTable(5);
        List<String> detailsColumns = Arrays.asList(
                "Cliente",
                "Documento",
                "Telefono",
                "Tipo Cliente",
                "Total facturado"
        );
        addTableHeader(pdfPTable, detailsColumns);
        for (ClientsReport client : clientsReports) {
            pdfPTable.addCell(client.getName() + " " + client.getLastName());
            pdfPTable.addCell(String.valueOf(client.getNro_doc()));
            pdfPTable.addCell(client.getPhone());
            pdfPTable.addCell(client.getClientType());
            pdfPTable.addCell(client.getTotalAmount().toString()); // Ajusta la representación según sea necesario
        }
        pdfPTable.setWidthPercentage(100);
        return pdfPTable;
    }
}

