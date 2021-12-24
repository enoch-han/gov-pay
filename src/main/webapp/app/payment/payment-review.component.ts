import { Router, RouteReuseStrategy } from '@angular/router';
import { PaymentService } from './payment.service';
import { Component, OnInit } from '@angular/core';
import { Payment } from './payment.model';

@Component({
  selector: 'jhi-payment-review',
  templateUrl: './payment-review.component.html',
  styleUrls: ['./payment-review.component.css'],
})
export class PaymentReviewComponent implements OnInit {
  currentPayment!: Payment;
  constructor(public paymentService: PaymentService, private router: Router) {}
  ngOnInit(): void {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (this.paymentService.currentpayment !== undefined) {
      this.currentPayment = this.paymentService.currentpayment;
    }
  }

  handleBack(): void {
    this.router.navigate(['/payment/payment-form']);
  }

  handleProceed(): void {
    // this.paymentService.getPayments().subscribe((value: Payment[]) => {
    //     if (value[0].id) {
    //         const preparedString = "/payment/payment-confirmation/".concat(value[0].id);
    //         this.router.navigate([preparedString]);
    //     }
    // })
    this.paymentService.savePayment().subscribe((value: Payment) => {
      if (value.id !== undefined) {
        const preparedString = '/payment/payment-confirmation/' + value.id;
        this.router.navigate([preparedString]);
      }
    });
  }
}
