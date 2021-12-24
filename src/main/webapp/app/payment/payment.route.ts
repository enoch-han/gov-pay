import { PaymentFormComponent } from './payment-form.component';
import { Authority } from '../config/authority.constants';
import { Routes } from '@angular/router';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';
import { PaymentConfirmationComponent } from './payment-confirmation';
import { PaymentReviewComponent } from './payment-review.component';
import { PaymentResolver } from './payment-resolver.service';

export const paymentFormRoute: Routes = [
  {
    path: 'payment-form',
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
    path: 'payment-confirmation/:id',
    component: PaymentConfirmationComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'payment',
    },
    resolve: { id: PaymentResolver },
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
