import { WorkingTime } from './../core/util/working-time.service';
import { WPaymentSuccessResponse } from './wpayment -success-response.model';
import { Wpayment } from './wpayment.model';
import { Mockbin } from './mockbin.model';
import { HttpClient } from '@angular/common/http';
import { Inject, Injectable, OnInit } from '@angular/core';
import { interval, Observable } from 'rxjs';
import { Payment } from './payment.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { DOCUMENT } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgbdModalContent } from './payment-review.component';
import { Idle } from 'idlejs';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  public currentpayment!: Payment;
  private resourceUrl = 'api/payments';
  // eslint-disable-next-line @typescript-eslint/member-ordering
  public hostedCheckoutID!: string;
  // eslint-disable-next-line @typescript-eslint/member-ordering
  public token!: string;
  private timer = interval(5000);
  private comparingString = '';

  constructor(
    private http: HttpClient,
    private applicationConfigService: ApplicationConfigService,
    private workingtime: WorkingTime,
    private router: Router,
    private modalService: NgbModal
  ) {
    this.router.events.pipe(filter((event: any) => event instanceof NavigationEnd)).subscribe((event: NavigationEnd) => {
      this.comparingString = event.url;
    });
    this.timer.subscribe((val: number) => {
      if (!this.workingtime.checkWorkingHour()) {
        if (this.comparingString !== '/not-working-hours') {
          router.navigate(['/not-working-hours/']);
        }
      }
    });
    //the snippet that controls the redirection if user is idle
    const idle = new Idle()
      .whenNotInteractive()
      .within(60)
      .do(() => this.openModal())
      .start();
  }

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

  getPayment(id: string): Observable<Mockbin> {
    // eslint-disable-next-line @typescript-eslint/ban-types
    return this.http.post<Mockbin>('/api/payments/getPaymentResponse', id);
  }

  getCompanyName(): Observable<Mockbin> {
    return this.http.get<Mockbin>('/api/payments/companyName/');
  }

  getLastPayment(): Observable<Mockbin> {
    return this.http.get<Mockbin>('/api/payments/lastPayment/');
  }

  getInitiatePayment(): Observable<Mockbin> {
    //returns the redirect url if succesful in WPaymentSuccessResponse object
    const copy: Payment = Object.assign({}, this.currentpayment);
    return this.http.post<Mockbin>('/api/payments/initiate', copy);
  }

  openModal(): void {
    const modalRef = this.modalService.open(NgbdModalContent);
    const url = 'http://www.google.com';
    modalRef.componentInstance.url = url;
  }
}
