import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Payment } from './payment.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  public currentpayment!: Payment;
  private resourceUrl = 'api/payments';

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  savePayment(): Observable<Payment> {
    const copy: Payment = Object.assign({}, this.currentpayment);
    return this.http.post<Payment>(this.applicationConfigService.getEndpointFor(this.resourceUrl), copy);
  }

  getPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(this.resourceUrl);
  }

  getPayment(id: string): Observable<Payment> {
    return this.http.get<Payment>('/api/payments/' + id);
  }
}
