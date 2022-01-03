import { Injectable } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, Resolve } from '@angular/router';
import { PaymentService } from './payment.service';

@Injectable()
export class PaymentResolver implements Resolve<any> {
  constructor(private paymentService: PaymentService) {}

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  resolve() {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return this.paymentService.getPayment(history.state.data);
  }
}
