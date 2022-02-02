/* eslint-disable no-console */
import { WPaymentSuccessResponse } from './wpayment -success-response.model';
import { BrowserModule, By } from '@angular/platform-browser';
/* eslint-disable prefer-const */
import { Mockbin } from './mockbin.model';
import { NgbModal, NgbActiveModal, NgbModalModule } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { TestBed, fakeAsync, tick, flushMicrotasks, flush } from '@angular/core/testing';
/* eslint-disable @typescript-eslint/no-inferrable-types */
import { ComponentFixture } from '@angular/core/testing';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { NgbdModalContent, PaymentReviewComponent } from './payment-review.component';
import { Payment } from './payment.model';
import { PaymentService } from './payment.service';
import { CommonModule } from '@angular/common';

jest.mock('@angular/router');
jest.mock('app/payment/payment.service');

function prepareTestPayment(choice: number = 2): Payment {
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

const testWPaymentResponse: WPaymentSuccessResponse = {
  RETURNMAC: 'ADSFDF',
  hostedCheckoutId: 'jojoj',
  partialRedirectUrl: 'some.com',
};

describe('Payment Review Component without any query params', () => {
  let reviewComponent: PaymentReviewComponent;
  let reviewFixture: ComponentFixture<PaymentReviewComponent>;
  let mockRouter: Router;
  let mockPaymentService: PaymentService;
  let param: Observable<Params> = of({});

  let modalComponent: NgbdModalContent;
  let modalFixture: ComponentFixture<NgbdModalContent>;

  const mockActivatedRoute = {
    queryParams: param,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommonModule, NgbModalModule],
      declarations: [PaymentReviewComponent, NgbdModalContent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        Router,
        NgbModal,
        {
          provide: PaymentService,
          useValue: {
            currentpayment: prepareTestPayment(1),
            getCompanyName: jest.fn(() => of(new Mockbin('ABC company'))),
            getLastPayment: jest.fn(() => of(new Mockbin('1643743784'))),
            getInitiatePayment: jest.fn(() => of(testWPaymentResponse)),
          },
        },
        {
          provide: ActivatedRoute,
          useValue: mockActivatedRoute,
        },
        NgbActiveModal,
      ],
    })
      .configureTestingModule({ declarations: [NgbdModalContent] })
      .overrideModule(BrowserModule, { set: { entryComponents: [NgbdModalContent] } });
  });

  beforeEach(() => {
    reviewFixture = TestBed.createComponent(PaymentReviewComponent);
    modalFixture = TestBed.createComponent(NgbdModalContent);
    reviewComponent = reviewFixture.componentInstance;
    modalComponent = modalFixture.componentInstance;
    mockRouter = TestBed.inject(Router);
  });

  describe('ngOnInit', () => {
    beforeEach(() => {
      // GIVEN
      reviewFixture.detectChanges();
    });

    it('should instantiate the current payment to a correct value', () => {
      // THEN
      expect(reviewComponent.currentPayment).toEqual(prepareTestPayment(2));
    });

    it('should instantiate the company name to a correct value', () => {
      //Then
      expect(reviewComponent.currentPayment.companyName).toEqual('ABC company');
    });

    it('should instantiate the last payment to a correct value', () => {
      //Then
      expect(reviewComponent.currentPayment.lastPayment).toEqual(1643743784);
    });
  });

  describe('test back button', () => {
    beforeEach(() => {
      // GIVEN
      reviewFixture.detectChanges();
    });

    it('when clicked should invoke handle clear method', () => {
      // GIVEN
      jest.spyOn(reviewFixture.componentInstance, 'handleBack');
      const backDea = reviewFixture.debugElement.query(By.css('#back_button'));

      // WHEN
      backDea.triggerEventHandler('click', null);

      // THEN
      expect(reviewFixture.componentInstance.handleBack).toHaveBeenCalled();
    });

    it('when clicked should go to correct path', () => {
      // GIVEN
      const backDea = reviewFixture.debugElement.query(By.css('#back_button'));

      // WHEN
      backDea.triggerEventHandler('click', null);

      // THEN
      expect(mockRouter.navigate).toHaveBeenLastCalledWith(['']);
    });
  });

  describe('test proceed button', () => {
    beforeEach(() => {
      // GIVEN
      reviewFixture.detectChanges();
    });

    it('should invoke the handle proceed method', () => {
      // GIVEN
      jest.spyOn(reviewFixture.componentInstance, 'handleProceed');
      const proceedDea = reviewFixture.debugElement.query(By.css('#proceed_button'));

      // WHEN
      proceedDea.triggerEventHandler('click', null);

      // THEN
      expect(reviewFixture.componentInstance.handleProceed).toHaveBeenCalled();
    });

    it('should invoke the open method', () => {
      // GIVEN
      jest.spyOn(reviewFixture.componentInstance, 'open');
      const proceedDea = reviewFixture.debugElement.query(By.css('#proceed_button'));

      // WHEN
      proceedDea.triggerEventHandler('click', null);

      // THEN
      expect(reviewFixture.componentInstance.open).toHaveBeenCalled();
    });

    it('should state the url variable to correct url', () => {
      // GIVEN
      const proceedDea = reviewFixture.debugElement.query(By.css('#proceed_button'));

      // WHEN
      proceedDea.triggerEventHandler('click', null);

      // THEN
      expect(reviewFixture.componentInstance.url).toEqual('https://payment.some.com');
    });
  });
  describe('test the modal', () => {
    beforeEach(() => {
      // GIVEN
      modalFixture.detectChanges();

      // WHEN
      const proceedDea = reviewFixture.debugElement.query(By.css('#proceed_button'));
      proceedDea.triggerEventHandler('click', null);
    });

    it('should open the modal', () => {
      // THEN
      expect(reviewFixture.componentInstance.modalService.hasOpenModals()).toBe(true);
    });

    it('should have the correct url', () => {
      // THEN
      expect(reviewFixture.componentInstance.modalRef.componentInstance.url).toEqual('https://payment.some.com');
    });

    it('should fire handle continue method when continue pressed', () => {
      // GIVEN
      jest.spyOn(modalFixture.componentInstance, 'handleContinue');
      const continueDea = modalFixture.debugElement.query(By.css('#continue_button'));

      // WHEN
      continueDea.triggerEventHandler('click', null);

      //THEN
      expect(modalFixture.componentInstance.handleContinue).toHaveBeenCalled();
    });

    it('should fire go to url method when continue pressed', () => {
      // GIVEN
      jest.spyOn(modalFixture.componentInstance, 'goToUrl');
      const continueDea = modalFixture.debugElement.query(By.css('#continue_button'));

      // WHEN
      continueDea.triggerEventHandler('click', null);

      //THEN
      expect(modalFixture.componentInstance.goToUrl).toHaveBeenCalled();
    });
  });
});

describe('test payment review with query payam', () => {
  let reviewFixture2: ComponentFixture<PaymentReviewComponent>;
  let mockRouter2: Router;
  let param2: Observable<Params> = of({ hostedCheckoutId: '1234' });

  const mockActivatedRoute = {
    queryParams: param2,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaymentReviewComponent, NgbdModalContent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: Router,
          useValue: {
            navigate: jest.fn(),
          },
        },
        NgbModal,
        {
          provide: PaymentService,
          useValue: {
            currentpayment: prepareTestPayment(1),
            getCompanyName: jest.fn(() => of(new Mockbin('ABC company'))),
            getLastPayment: jest.fn(() => of(new Mockbin('1643743784'))),
            getInitiatePayment: jest.fn(() => of(testWPaymentResponse)),
          },
        },
        {
          provide: ActivatedRoute,
          useValue: mockActivatedRoute,
        },
        NgbActiveModal,
      ],
    });
  });

  beforeEach(() => {
    reviewFixture2 = TestBed.createComponent(PaymentReviewComponent);
    mockRouter2 = TestBed.inject(Router);
  });

  it('should go to payment confirmations page', () => {
    reviewFixture2.detectChanges();

    expect(mockRouter2.navigate).toHaveBeenCalledWith(['/payment-confirmation'], { state: { data: '1234' } });
  });
});
