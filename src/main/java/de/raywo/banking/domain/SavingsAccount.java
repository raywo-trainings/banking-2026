package de.raywo.banking.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SavingsAccount extends Account {

  private float creditInterestRate;


  public SavingsAccount(String iban, Customer owner) {
    super(iban, owner);
    this.creditInterestRate = 0.0f;
  }


  public float getCreditInterestRate() {
    return creditInterestRate;
  }


  public void setCreditInterestRate(float creditInterestRate) {
    this.creditInterestRate = creditInterestRate;
  }


  @Override
  public String toString() {
    DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(2);

    return super.toString() + ", Habenzins: " + df.format(creditInterestRate);
  }

}
