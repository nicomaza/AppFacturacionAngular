import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';

import { InvoiceDto } from '../models/InvoiceDto';
import { Invoice } from '../models/Invoice';
import { FormGroup } from '@angular/forms';
import { RequestInvoiceDto } from '../models/requestInvoiceDTO';
import { Detail } from '../models/Detail';
import { InvoiceDetail } from '../models/InvoiceDetail';
import { ToolsService } from './tools.service';

@Injectable({
  providedIn: 'root',
})
export class InvoiceService {
  newInvoice?: InvoiceDto;
  totalPay: number = 0;
  apiUrl: string = 'http://localhost:8081/api/v1/invoice';

  requestInvoice?: RequestInvoiceDto;

  constructor(private http: HttpClient, private toolsService: ToolsService) { }

  loadInvoice(body: RequestInvoiceDto): Observable<RequestInvoiceDto> {
    return this.http.post<RequestInvoiceDto>(
      'https://api.example.com/data',
      body
    );
  }

  getInvoices(): Observable<InvoiceDto[]> {
    return this.http.get<InvoiceDto[]>(
      `${this.apiUrl}/all`
    );
  }
  getInvoicesFiltered(
    dateFrom: string | null,
    dateTo: string | null,
    client: string
  ): Observable<InvoiceDto[]> {
    let params = new HttpParams();

    if (dateFrom !== null && dateFrom !== '') {
      const formattedDateFrom: string = this.toolsService.formatDate(dateFrom);

      params = params.set('dateFrom', formattedDateFrom);
    }

    if (dateTo !== null && dateTo !== '') {
      const formattedDateTo: string = this.toolsService.formatDate(dateTo);
      params = params.set('dateTo', formattedDateTo);
    }

    if (client !== '') {
      params = params.set('clientId', client);
    }

    // Construir la URL con parámetros codificados
    const apiUrl = `${this.apiUrl}/all/filtered?${params.toString()}`;

    // Hacer la solicitud HTTP con parámetros
    return this.http.get<InvoiceDto[]>(apiUrl).pipe(
      catchError((error) => {
        // Manejar el error aquí (puedes logearlo o realizar otras acciones)
        console.error('Error en la solicitud HTTP:', error);
        throw error; // Puedes lanzar el error nuevamente o realizar alguna acción específica.
      })
    );
  }

  setTotalpay(num: number) {
    this.totalPay = num;
  }
  getTotalpay() {
    return this.totalPay;
  }

  createInvoice(invoice: Invoice): Observable<Invoice> {
    return this.http.post<Invoice>(
      this.apiUrl,
      invoice
    );
  }

  deleteInvoice(id_invoice:number) :Observable<any>{
    const url = `${this.apiUrl}/delete/${id_invoice}`;
    return this.http.put(url,{})
  }

  getDetailInvoices(id_invoice:number):Observable<InvoiceDetail[]>{
    return this.http.get<InvoiceDetail[]>(`${this.apiUrl}/details/` + id_invoice)
  }
  generateInvoicePdf(id: number): Observable<Blob> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/pdf'
    });
    return this.http.get(`${this.apiUrl}/generate-pdf-bytes/${id}`, {
      responseType: 'blob',
      headers: headers
    });
  }
}
