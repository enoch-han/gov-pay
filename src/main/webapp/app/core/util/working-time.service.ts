/* eslint-disable no-console */
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class WorkingTime {
  public startTime!: Date;
  public endTime!: Date;
  public currentTime!: Date;
  constructor() {
    this.startTime = new Date();
    this.startTime.setUTCHours(3);
    this.startTime.setUTCMinutes(0);
    this.endTime = new Date();
    this.endTime.setUTCHours(23);
    this.endTime.setUTCMinutes(0);
    this.currentTime = new Date();
  }

  getCurrentTime(): Date {
    return this.currentTime;
  }
  checkWorkingHour(): boolean {
    this.currentTime = new Date();
    console.log(this.currentTime.getUTCHours());
    if (this.currentTime.getUTCHours() >= this.startTime.getUTCHours() && this.currentTime.getUTCHours() < this.endTime.getUTCHours()) {
      return true;
    } else {
      return false;
    }
  }
}
