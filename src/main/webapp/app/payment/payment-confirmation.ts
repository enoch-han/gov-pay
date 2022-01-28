import { Payment } from './payment.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { LoginService } from 'app/login/login.service';
import { PaymentService } from './payment.service';
import { Wpayment } from './wpayment.model';
import { Mockbin } from './mockbin.model';

@Component({
  selector: 'jhi-payment-confirmation',
  templateUrl: './payment-confirmation.component.html',
  styleUrls: ['./payment-confirmation.component.css'],
})
export class PaymentConfirmationComponent implements OnInit {
  id!: string | undefined;
  amount!: number | undefined;
  wPayment!: Wpayment;
  openAPIEnabled?: boolean;
  inProduction?: boolean;
  account: Account | null = null;
  constructor(
    private route: ActivatedRoute,
    private paymentService: PaymentService,
    private router: Router,
    private accountService: AccountService,
    private loginService: LoginService,
    private profileService: ProfileService
  ) {}
  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    // try to log in automatically
    this.accountService.identity().subscribe(() => {
      if (!this.accountService.isAuthenticated()) {
        this.loginService
          .login({
            username: 'user',
            password: 'user',
            rememberMe: false,
          })
          .subscribe(() => {
            this.accountService.identity().subscribe();
          });
      }
    });
    this.paymentService.getPayment(history.state.data).subscribe((value: Mockbin) => {
      // eslint-disable-next-line no-console
      console.log(value.text);
      // eslint-disable-next-line no-console
      console.log(history.state.data);
      const sessionValue = localStorage.getItem('payment');
      // eslint-disable-next-line no-console
      console.log('session value down');
      // eslint-disable-next-line no-console
      console.log(sessionValue);
      if (sessionValue !== null) {
        const copy = JSON.parse(sessionValue) as Payment;
        copy.paymentId = value.text;
        this.paymentService.currentpayment = copy;
        this.id = this.paymentService.currentpayment.paymentId;
        this.amount = this.paymentService.currentpayment.paymentAmount;
        // eslint-disable-next-line no-console
        console.log('current payment down');
        // eslint-disable-next-line no-console
        console.log(this.paymentService.currentpayment);
        this.paymentService.savePayment().subscribe((payment: Payment) => {
          // eslint-disable-next-line no-console
          console.log('payment created');
          // eslint-disable-next-line no-console
          console.log(payment);
        });
      }
    });
  }

  handleClose(): void {
    this.router.navigate(['']);
  }
}
