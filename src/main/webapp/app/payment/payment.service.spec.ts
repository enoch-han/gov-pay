import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Payment } from 'app/payment/payment.model';
import { PaymentService } from 'app/payment/payment.service';

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

describe('Payment Service', () => {
  let paymentService: PaymentService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PaymentService],
    });
    paymentService = TestBed.inject(PaymentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Save Payment', () => {
    it('should call payment saving with correct endpoint and value', () => {
      // GIVEN
      const testPayment = prepareTestPayment(2);
      paymentService.currentpayment = testPayment;

      // WHEN
      paymentService.savePayment().subscribe();
      const testRequest = httpMock.expectOne({ method: 'POST', url: '/api/payments' });
      testRequest.flush({});

      // THEN
      expect(testRequest.request.body).toEqual(testPayment);
    });
  });

  describe('Get Payments', () => {
    it('should call get payments with correct endpoint', () => {
      // WHEN
      paymentService.getPayments().subscribe();

      // THEN
      httpMock.expectOne({ method: 'GET', url: 'api/payments' });
    });
  });

  describe('Get Payment', () => {
    it('should call get payment with correct endpoint and value', () => {
      // GIVEN
      const testId = '1';

      // WHEN
      paymentService.getPayment(testId).subscribe();
      const testRequest = httpMock.expectOne({ method: 'POST', url: '/api/payments/getPaymentResponse' });
      testRequest.flush({});

      // THEN
      expect(testRequest.request.body).toEqual(testId);
    });
  });

  describe('Get Company Name', () => {
    it('should call get company name with correct endpoint', () => {
      // WHEN
      paymentService.getCompanyName().subscribe();

      // THEN
      httpMock.expectOne({ method: 'GET', url: '/api/payments/companyName/' });
    });
  });

  describe('Get Last Payment', () => {
    it('should call get last payment with correct endpoint', () => {
      // WHEN
      paymentService.getLastPayment().subscribe();

      // THEN
      httpMock.expectOne({ method: 'GET', url: '/api/payments/lastPayment/' });
    });
  });

  describe('Get Initiate Payment', () => {
    it('should call get initiate payment with correct endpoint and value', () => {
      // GIVEN
      const testPayment = prepareTestPayment(2);
      paymentService.currentpayment = testPayment;

      // WHEN
      paymentService.getInitiatePayment().subscribe();
      const testRequest = httpMock.expectOne({ method: 'POST', url: '/api/payments/initiate' });
      testRequest.flush({});

      // THEN
      expect(testRequest.request.body).toEqual(testPayment);
    });
  });
});
