import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { PaymentService } from './payment.service';
import { Payment } from './payment.model';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-payment-form',
  templateUrl: './payment-form.component.html',
  styleUrls: ['./payment-form.component.css'],
})
export class PaymentFormComponent implements OnInit {
  cik!: string | undefined;
  ccc!: string | undefined;
  paymentAmount!: number | undefined;
  name!: string | undefined;
  email!: string | undefined;
  phone!: string | undefined;
  currentPayment!: Payment;
  paymentFormGroup!: FormGroup;
  cikFormControl!: FormControl;
  cccFormControl!: FormControl;
  paymentAmountFormControl!: FormControl;
  nameFormControl!: FormControl;
  emailFormControl!: FormControl;
  phoneFormControl!: FormControl;

  ngOnInit(): void {
    this.cikFormControl = new FormControl('', [
      Validators.required,
      Validators.pattern('^[0-9]*$'),
      Validators.maxLength(10),
      Validators.minLength(7),
    ]);
    this.cccFormControl = new FormControl('', [Validators.required, Validators.pattern('^[a-zA-Z0-9_]*$')]);
    this.paymentAmountFormControl = new FormControl('', [Validators.required, Validators.min(5)]);
    this.nameFormControl = new FormControl('', [Validators.required, Validators.pattern('^[a-zA-Z0-9_]*$')]);
    this.emailFormControl = new FormControl('', [Validators.required, Validators.email]);
    this.phoneFormControl = new FormControl('', [
      Validators.required,
      Validators.pattern('^[0-9]*$'),
      Validators.minLength(10),
      Validators.maxLength(10),
    ]);
    this.paymentFormGroup = new FormGroup({
      cikFormControl: this.cikFormControl,
      cccFormControl: this.cccFormControl,
      paymentAmountFormControl: this.paymentAmountFormControl,
      nameFormControl: this.nameFormControl,
      emailFormControl: this.emailFormControl,
      phoneFormControl: this.phoneFormControl,
    });
  }

  // eslint-disable-next-line @typescript-eslint/member-ordering
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
    this.router.navigate(['/payment-review']);
  }

  handleClear(): void {
    this.cik = undefined;
    this.ccc = undefined;
    this.paymentAmount = undefined;
    this.name = undefined;
    this.email = undefined;
    this.phone = undefined;
  }

  formatCik(): void {
    // adds leading 0's to the cik if it's length is less than 10
    if (this.cik != null) {
      if (this.cik !== '') {
        if (this.cik.length >= 7 || this.cik.length < 10) {
          this.cik = this.padLeft(this.cik, '0', 10);
        }
      }
    }
  }

  padLeft(text: string, padChar: string, size: number): string {
    return (String(padChar).repeat(size) + text).substr(size * -1, size);
  }

  formatPaymentAmount(): void {
    if (this.paymentAmount != null) {
      this.paymentAmount = +this.paymentAmount.toFixed(2);
    }
  }
}
