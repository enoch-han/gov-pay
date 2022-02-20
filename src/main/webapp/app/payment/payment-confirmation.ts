import { Payment } from './payment.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { LoginService } from 'app/login/login.service';
import { PaymentService } from './payment.service';
import { Wpayment } from './wpayment.model';

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
    this.paymentService.getPayment(history.state.data).subscribe((value: Wpayment) => {
      this.wPayment = value;

      const sessionValue = localStorage.getItem('payment');

      if (sessionValue !== null) {
        const copy = JSON.parse(sessionValue) as Payment;
        copy.paymentId = this.wPayment.createdPaymentOutput.payment.id;
        copy.expiryDate = this.wPayment.createdPaymentOutput.payment.paymentOutput.cardPaymentMethodSpecificOutput.card.expiryDate;
        this.paymentService.currentpayment = copy;
        this.paymentService.savePayment().subscribe();
      }
    });
  }

  handleClose(): void {
    this.router.navigate(['']);
  }
}
