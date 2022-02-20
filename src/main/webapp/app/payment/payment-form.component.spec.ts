import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { PaymentFormComponent } from './payment-form.component';
import { Payment } from './payment.model';
import { PaymentService } from './payment.service';

jest.mock('@angular/router');

function prepareTestPayment(choice = 2): Payment {
  // returns a test payment
  const testPayment = new Payment();
  testPayment.cik = '1234567';
  testPayment.ccc = 'asdf12';
  testPayment.paymentAmount = 12;
  testPayment.name = 'natan';
  testPayment.email = 'natan@gmail.com';
  testPayment.phoneNumber = '1234567897';
  if (choice === 1) {
    // returns form completed payment
    return testPayment;
  } else if (choice === 2) {
    // returns review completed payment
    testPayment.lastPayment = 1643743784;
    testPayment.companyName = 'ABC company';
    return testPayment;
  } else {
    // returns database completed payment
    testPayment.paymentId = '12adf-adf56-48ads-adf58';
    testPayment.lastPayment = 1643743784;
    testPayment.companyName = 'ABC company';
    testPayment.id = '5';
    return testPayment;
  }
}

describe('Payment Form Component', () => {
  let component: PaymentFormComponent;
  let fixture: ComponentFixture<PaymentFormComponent>;
  let mockRouter: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule],
      declarations: [PaymentFormComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        FormBuilder,
        Router,
        {
          provide: PaymentService,
          useValue: {
            currentpayment: prepareTestPayment(2),
          },
        },
      ],
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentFormComponent);
    component = fixture.componentInstance;
    mockRouter = TestBed.inject(Router);
  });

  describe('ngOnInit', () => {
    beforeEach(() => {
      // WHEN
      // component.ngOnInit();
      fixture.detectChanges();
    });
    it('should instantiate the form group', () => {
      // THEN
      expect(component.paymentFormGroup).not.toBeNull();
      expect(component.paymentFormGroup).not.toBeUndefined();
    });
    it('should instatiate all the form controls', () => {
      // THEN
      expect(component.cikFormControl).not.toBeNull();
      expect(component.cikFormControl).not.toBeUndefined();
      expect(component.cccFormControl).not.toBeNull();
      expect(component.cccFormControl).not.toBeUndefined();
      expect(component.paymentAmountFormControl).not.toBeNull();
      expect(component.paymentAmountFormControl).not.toBeUndefined();
      expect(component.nameFormControl).not.toBeNull();
      expect(component.nameFormControl).not.toBeUndefined();
      expect(component.emailFormControl).not.toBeNull();
      expect(component.emailFormControl).not.toBeUndefined();
      expect(component.phoneFormControl).not.toBeNull();
      expect(component.phoneFormControl).not.toBeUndefined();
    });
  });

  describe('input fields test', () => {
    it('cik input field should call the format cik method on focus out', () => {
      // GIVEN
      jest.spyOn(fixture.componentInstance, 'formatCik');
      const testCik = '12345678';
      const cikDea = fixture.debugElement.query(By.css('#field_cik'));
      cikDea.nativeElement.value = testCik;
      fixture.detectChanges();

      // WHEN
      cikDea.triggerEventHandler('focusout', null);

      // THEN
      expect(fixture.componentInstance.formatCik).toHaveBeenCalled();
    });

    it('payment amount field should call the format payment amounts method on focus out', () => {
      // GIVEN
      jest.spyOn(fixture.componentInstance, 'formatPaymentAmount');
      const testAmount = '12.12354';
      const amountDea = fixture.debugElement.query(By.css('#field_payment_amount'));
      amountDea.nativeElement.value = testAmount;
      fixture.detectChanges();

      // WHEN
      amountDea.triggerEventHandler('focusout', null);

      // THEN
      expect(fixture.componentInstance.formatPaymentAmount).toHaveBeenCalled();
    });
  });

  describe('Button tests', () => {
    it('next button should fire up submit which inturn fires up on next method', () => {
      // GIVEN
      jest.spyOn(fixture.componentInstance, 'onNext');
      const testPayment = prepareTestPayment(2);
      fixture.debugElement.query(By.css('#field_cik')).nativeElement.value = testPayment.cik;
      fixture.debugElement.query(By.css('#field_ccc')).nativeElement.value = testPayment.ccc;
      fixture.debugElement.query(By.css('#field_payment_amount')).nativeElement.value = testPayment.paymentAmount;
      fixture.debugElement.query(By.css('#field_name')).nativeElement.value = testPayment.name;
      fixture.debugElement.query(By.css('#field_email')).nativeElement.value = testPayment.email;
      fixture.debugElement.query(By.css('#field_phone')).nativeElement.value = testPayment.phoneNumber;
      const formDea = fixture.debugElement.query(By.css('form'));
      fixture.detectChanges();

      // WHEN
      formDea.triggerEventHandler('ngSubmit', null);

      // THEN
      expect(fixture.componentInstance.onNext).toHaveBeenCalled();
    });

    it('clear button should fire up on handle clear method', () => {
      // GIVEN
      jest.spyOn(fixture.componentInstance, 'handleClear');
      const clearDea = fixture.debugElement.query(By.css('#clear_button'));

      // WHEN
      clearDea.triggerEventHandler('click', null);

      // THEN
      expect(fixture.componentInstance.handleClear).toHaveBeenCalled();
    });

    it('should go to the correct url when next is clicked', () => {
      // GIVEN
      const testPayment = prepareTestPayment(2);
      fixture.debugElement.query(By.css('#field_cik')).nativeElement.value = testPayment.cik;
      fixture.debugElement.query(By.css('#field_ccc')).nativeElement.value = testPayment.ccc;
      fixture.debugElement.query(By.css('#field_payment_amount')).nativeElement.value = testPayment.paymentAmount;
      fixture.debugElement.query(By.css('#field_name')).nativeElement.value = testPayment.name;
      fixture.debugElement.query(By.css('#field_email')).nativeElement.value = testPayment.email;
      fixture.debugElement.query(By.css('#field_phone')).nativeElement.value = testPayment.phoneNumber;
      const formDea = fixture.debugElement.query(By.css('form'));
      fixture.detectChanges();

      // WHEN
      formDea.triggerEventHandler('ngSubmit', null);

      // THEN
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/payment-review']);
    });
  });
});
