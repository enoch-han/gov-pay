import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { PaymentService } from './payment.service';
import { Payment } from './payment.model';

@Component({
  selector: 'jhi-payment-form',
  templateUrl: './payment-form.component.html',
  styleUrls: ['./payment-form.component.css'],
})
export class PaymentFormComponent {
  cik!: string | undefined;
  ccc!: string | undefined;
  paymentAmount!: number | undefined;
  name!: string | undefined;
  email!: string | undefined;
  phone!: string | undefined;
  currentPayment!: Payment;
  constructor(private router: Router, public paymentService: PaymentService) {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (this.paymentService.currentpayment !== undefined) {
      this.cik = this.paymentService.currentpayment.cik;
      this.ccc = this.paymentService.currentpayment.ccc;
      this.paymentAmount = this.paymentService.currentpayment.paymentAmount;
      this.name = this.paymentService.currentpayment.name;
      this.email = this.paymentService.currentpayment.email;
      this.phone = this.paymentService.currentpayment.phoneNumber;
    }
  }
  onNext(): void {
    this.currentPayment = new Payment(this.cik, this.ccc, this.paymentAmount, this.name, this.email, this.phone);
    this.paymentService.currentpayment = this.currentPayment;
    this.router.navigate(['/payment/payment-review']);
  }
}
