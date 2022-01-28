import { WorkingTime } from './../core/util/working-time.service';
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TimeoutGuard implements CanActivate {
  constructor(private router: Router, private workingTime: WorkingTime) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.workingTime.checkWorkingHour()) {
      return true;
    } else {
      this.router.navigate(['/not-working-hours']);
      return false;
    }
  }
}
