import { WPaymentSuccessResponse } from './wpayment -success-response.model';
import { Mockbin } from './mockbin.model';
import { Router, RouteReuseStrategy, ActivatedRoute } from '@angular/router';
import { PaymentService } from './payment.service';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { Payment } from './payment.model';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DOCUMENT } from '@angular/common';
import { JsonpClientBackend } from '@angular/common/http';

@Component({
  selector: 'jhi-payment-review',
  templateUrl: './payment-review.component.html',
  styleUrls: ['./payment-review.component.css'],
})
export class PaymentReviewComponent implements OnInit {
  currentPayment!: Payment;
  url!: string;
  constructor(
    public paymentService: PaymentService,
    private router: Router,
    private modalService: NgbModal,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (this.paymentService.currentpayment !== undefined) {
      this.currentPayment = this.paymentService.currentpayment;
      this.paymentService.getCompanyName().subscribe((value: Mockbin) => {
        this.currentPayment.companyName = value.text;
      });
      this.paymentService.getLastPayment().subscribe((value: Mockbin) => {
        this.currentPayment.lastPayment = +value.text;
      });
    }
    this.route.queryParams.subscribe(params => {
      if (params.hostedCheckoutId) {
        //this.paymentService.hostedCheckoutID = params.hostedCheckoutId;
        this.router.navigate(['/payment-confirmation'], { state: { data: params.hostedCheckoutId } });
      }
    });
  }

  handleBack(): void {
    this.router.navigate(['']);
  }
  open(): void {
    const modalRef = this.modalService.open(NgbdModalContent);
    this.url = 'https://payment.'.concat(this.url);
    modalRef.componentInstance.url = this.url;
  }

  handleProceed(): void {
    // this.paymentService.getPayments().subscribe((value: Payment[]) => {
    //     if (value[0].id) {
    //         const preparedString = "/payment/payment-confirmation/".concat(value[0].id);
    //         this.router.navigate([preparedString]);
    //     }
    // })
    // this.paymentService.savePayment().subscribe((value: Payment) => {
    //   if (value.id !== undefined) {
    //     const preparedString = '/payment/payment-confirmation/' + value.id;
    //     this.router.navigate([preparedString]);
    //   }
    // });
    this.paymentService.currentpayment = this.currentPayment;
    localStorage.setItem('payment', JSON.stringify(this.paymentService.currentpayment));

    this.paymentService.getInitiatePayment().subscribe((value: WPaymentSuccessResponse) => {
      if (value.partialRedirectUrl !== 'error') {
        this.url = value.partialRedirectUrl;
        this.open();
      }
    });
  }
}
//-----------------------------------------

@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'ngbd-modal-content',
  templateUrl: './redirect-modal.component.html',
})
// eslint-disable-next-line @angular-eslint/component-class-suffix
export class NgbdModalContent {
  @Input() url: any;
  continuePressed = false;

  startValue = 10;
  constructor(public activeModal: NgbActiveModal, private modalService: NgbModal, @Inject(DOCUMENT) private document: Document) {
    this.initiateCountdown();
  }

  initiateCountdown(): void {
    setInterval(() => {
      this.checkValue();
      this.startValue = this.startValue - 1;
    }, 1000);
  }

  checkValue(): void {
    if (this.startValue === 0 && this.continuePressed === false) {
      //alert("countdown finished");
      this.modalService.dismissAll();
      this.goToUrl();
    }
  }

  goToUrl(): void {
    this.document.location.href = this.url;
  }
  handleContinue(): void {
    this.continuePressed = true;
    this.modalService.dismissAll();
    this.goToUrl();
  }
}
