import { WorkingTime } from './../core/util/working-time.service';
import { NotWorkingHoursComponent } from './not-working-hours.component';
import { PaymentResolver } from './payment-resolver.service';
import { PaymentService } from './payment.service';
import { PaymentConfirmationComponent } from './payment-confirmation';
import { PaymentReviewComponent } from './payment-review.component';
import { PaymentFormComponent } from './payment-form.component';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { notWorkingHoursRoute, paymentConfirmationRoute, paymentFormRoute, paymentReviewRoute } from './payment.route';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TimeoutGuard } from './timout.guard';

const ROUTES = [...paymentFormRoute, ...paymentReviewRoute, ...paymentConfirmationRoute, ...notWorkingHoursRoute];

@NgModule({
  imports: [RouterModule.forChild(ROUTES), FormsModule, CommonModule, ReactiveFormsModule, HttpClientModule],
  declarations: [PaymentFormComponent, PaymentReviewComponent, PaymentConfirmationComponent, NotWorkingHoursComponent],
  entryComponents: [PaymentFormComponent, PaymentReviewComponent, PaymentConfirmationComponent, NotWorkingHoursComponent],
  providers: [PaymentService, PaymentResolver, TimeoutGuard],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  exports: [PaymentFormComponent, PaymentReviewComponent, PaymentConfirmationComponent, NotWorkingHoursComponent],
})
export class PaymentModule {}
