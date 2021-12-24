import { User } from 'app/entities/user/user.model';
import { UserService } from './../entities/user/user.service';

export class Payment {
  public id?: string;
  public user?: User;
  public lastPayment?: number;
  public companyName?: string;

  constructor(
    public cik?: string,
    public ccc?: string,
    public paymentAmount?: number,
    public name?: string,
    public email?: string,
    public phoneNumber?: string
  ) {
    this.companyName = 'ABCcompany';
    this.lastPayment = Date.now();
  }
}
