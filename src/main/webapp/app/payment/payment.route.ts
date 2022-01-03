import { PaymentFormComponent } from './payment-form.component';
import { Authority } from '../config/authority.constants';
import { Routes } from '@angular/router';
import { PaymentConfirmationComponent } from './payment-confirmation';
import { PaymentReviewComponent } from './payment-review.component';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

export const paymentFormRoute: Routes = [
  {
    path: '',
    component: PaymentFormComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'payment',
    },
    canActivate: [UserRouteAccessService],
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
    canActivate: [UserRouteAccessService],
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
    canActivate: [UserRouteAccessService],
  },
];
