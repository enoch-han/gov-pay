import { PaymentResolver } from './payment-resolver.service';
import { PaymentService } from './payment.service';
import { PaymentConfirmationComponent } from './payment-confirmation';
import { PaymentReviewComponent } from './payment-review.component';
import { PaymentFormComponent } from './payment-form.component';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { paymentConfirmationRoute, paymentFormRoute, paymentReviewRoute } from './payment.route';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

const ROUTES = [...paymentFormRoute, ...paymentReviewRoute, ...paymentConfirmationRoute];

@NgModule({
  imports: [RouterModule.forChild(ROUTES), FormsModule, CommonModule, ReactiveFormsModule, HttpClientModule],
  declarations: [PaymentFormComponent, PaymentReviewComponent, PaymentConfirmationComponent],
  entryComponents: [PaymentFormComponent, PaymentReviewComponent, PaymentConfirmationComponent],
  providers: [PaymentService, PaymentResolver],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class PaymentModule {}
