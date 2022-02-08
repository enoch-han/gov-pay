export class Payment {
  public id?: string;
  public paymentId?: string;
  public lastPayment?: number;
  public companyName?: string;
  public expiryDate?: string;

  constructor(
    public cik?: string,
    public ccc?: string,
    public paymentAmount?: number,
    public name?: string,
    public email?: string,
    public phoneNumber?: string
  ) {}
}
