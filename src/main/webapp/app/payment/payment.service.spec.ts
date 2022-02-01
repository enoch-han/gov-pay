import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
/* eslint-disable @typescript-eslint/no-inferrable-types */
import { Payment } from './payment.model';
import { PaymentService } from './payment.service';

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

describe('Payment Service', () => {
  let paymentService: PaymentService;
  let httpMock: HttpTestingController;
  let applicationConfigService: ApplicationConfigService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PaymentService],
    });
    paymentService = TestBed.inject(PaymentService);
    httpMock = TestBed.inject(HttpTestingController);
    applicationConfigService = TestBed.inject(ApplicationConfigService);
  });

  describe('Save Payment', () => {
    it('should call payment saving endpoint wiht correct values', () => {
      // GIVEN
      const testPayment = prepareTestPayment();
      paymentService.currentpayment = testPayment;

      // WHEN
      paymentService.savePayment().subscribe();
      const testRequest = httpMock.expectOne({ method: 'POST', url: applicationConfigService.getEndpointFor('api/payments') });
      testRequest.flush({});

      // THEN
      expect(testRequest.request.body).toEqual(testPayment);
    });
  });
});
