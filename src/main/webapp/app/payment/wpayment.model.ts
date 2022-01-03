export interface Wpayment {
  createdPaymentOutput: {
    payment: {
      id: string;
      hostedCheckoutSpecificOutput: {
        hostedCheckoutId: string;
        variant: string;
      };
      paymentOutput: {
        amountOfMoney: {
          amount: number;
          currentcyCode: string;
        };
        references: {
          paymentReference: string;
        };
        paymentMethod: string;
        cardPaymentMethodSpecificOutput: {
          paymentProductId: number;
          authorisationCode: string;
          card: {
            cardNumber: string;
            expiryDate: string;
          };
          fraudResults: {
            avsResults: string;
            cvvResult: string;
            fraudServiceResult: string;
          };
          threeDSecureResults: {
            authenticationAmount: {
              currencyCode: string;
              amount: number;
            };
          };
        };
      };
      status: string;
      statusOutput: {
        isCancellable: boolean;
        statusCode: number;
        statusCodeChangeDateTime: string;
        isAuthorized: boolean;
      };
    };
    paymentCreationReferences: {
      additionalReference: string;
      externalReference: string;
    };
    tokens: string;
  };
  status: string;
}
