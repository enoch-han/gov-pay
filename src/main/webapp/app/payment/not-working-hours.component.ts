import { WorkingTime } from './../core/util/working-time.service';
import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-not-working-hours',
  templateUrl: './not-working-hours.component.html',
  styleUrls: ['./payment-confirmation.component.css'],
})
export class NotWorkingHoursComponent implements OnInit {
  openAPIEnabled?: boolean;
  inProduction?: boolean;
  account: Account | null = null;
  public currentTime!: Date;
  constructor(
    public workingTime: WorkingTime,
    private accountService: AccountService,
    private loginService: LoginService,
    private profileService: ProfileService
  ) {
    this.currentTime = new Date();
    // eslint-disable-next-line no-console
    console.log(this.currentTime);
  }
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
  }
}
