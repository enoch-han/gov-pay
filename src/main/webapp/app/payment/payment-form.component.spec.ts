import { PaymentFormComponent } from './payment-form.component';
import { Payment } from './payment.model';

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

describe('Payment Form Component', () => {
  let component: PaymentFormComponent;
});
