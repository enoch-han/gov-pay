import { TimeoutGuard } from './timout.guard';
import { PaymentFormComponent } from './payment-form.component';
import { Authority } from '../config/authority.constants';
import { Routes } from '@angular/router';
import { PaymentConfirmationComponent } from './payment-confirmation';
import { PaymentReviewComponent } from './payment-review.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NotWorkingHoursComponent } from './not-working-hours.component';

export const paymentFormRoute: Routes = [
  {
    path: '',
    component: PaymentFormComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'payment',
    },
    canActivate: [UserRouteAccessService, TimeoutGuard],
  },
];

export const paymentConfirmationRoute: Routes = [
  {
    path: 'payment-confirmation',
    component: PaymentConfirmationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'payment',
    },
    canActivate: [UserRouteAccessService, TimeoutGuard],
  },
];

export const paymentReviewRoute: Routes = [
  {
    path: 'payment-review',
    component: PaymentReviewComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'payment',
    },
    canActivate: [UserRouteAccessService, TimeoutGuard],
  },
];

export const notWorkingHoursRoute: Routes = [
  {
    path: 'not-working-hours',
    component: NotWorkingHoursComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'payment',
    },
    canActivate: [UserRouteAccessService],
  },
];
