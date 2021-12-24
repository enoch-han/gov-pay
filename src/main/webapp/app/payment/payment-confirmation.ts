import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot } from '@angular/router';
import { Payment } from './payment.model';
import { PaymentService } from './payment.service';

@Component({
  selector: 'jhi-payment-confirmation',
  templateUrl: './payment-confirmation.component.html',
  styleUrls: ['./payment-confirmation.component.css'],
})
export class PaymentConfirmationComponent implements OnInit {
  id!: string | undefined;
  amount!: number | undefined;
  constructor(private route: ActivatedRoute, private paymentService: PaymentService) {}
  ngOnInit(): void {
    this.route.data.forEach(data => {
      this.id = data['id'];
      this.amount = this.paymentService.currentpayment.paymentAmount;
    });
  }
}
