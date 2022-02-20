import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router';
import { Observable } from 'rxjs';
import { PaymentService } from './payment.service';
import { Wpayment } from './wpayment.model';

@Injectable()
export class PaymentResolver implements Resolve<any> {
  constructor(private paymentService: PaymentService) {}

  resolve(): Observable<Wpayment> {
    return this.paymentService.getPayment(history.state.data);
  }
}
