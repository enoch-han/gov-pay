import { WPaymentSuccessResponse } from './wpayment -success-response.model';
import { Wpayment } from './wpayment.model';
import { Mockbin } from './mockbin.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Payment } from './payment.model';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  public currentpayment!: Payment;
  private resourceUrl = 'api/payments';
  // eslint-disable-next-line @typescript-eslint/member-ordering
  public hostedCheckoutID!: string;

  constructor(private http: HttpClient) {}

  savePayment(): Observable<Payment> {
    const copy: Payment = Object.assign({}, this.currentpayment);
    // eslint-disable-next-line no-console
    console.log('in save payment method');
    // eslint-disable-next-line no-console
    console.log(copy);
    return this.http.post<Payment>('/api/payments', copy);
  }

  getPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(this.resourceUrl);
  }

  getPayment(id: string): Observable<Wpayment> {
    // eslint-disable-next-line @typescript-eslint/ban-types
    return this.http.post<Wpayment>('/api/payments/getPaymentResponse', id);
  }

  getCompanyName(): Observable<Mockbin> {
    return this.http.get<Mockbin>('/api/payments/companyName/');
  }

  getLastPayment(): Observable<Mockbin> {
    return this.http.get<Mockbin>('/api/payments/lastPayment/');
  }

  getInitiatePayment(): Observable<WPaymentSuccessResponse> {
    // returns the redirect url if succesful in WPaymentSuccessResponse object
    const copy: Payment = Object.assign({}, this.currentpayment);
    return this.http.post<WPaymentSuccessResponse>('/api/payments/initiate', copy);
  }
}
