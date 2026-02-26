package de.raywo.banking.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class SavingsAccount extends Account {

  private float interestRate;


  public SavingsAccount(String iban, Customer owner) {
    super(iban, owner);
    this.interestRate = 0.0f;
  }


  @Override
  public float getInterestRate() {
    return interestRate;
  }


  @Override
  public void setInterestRate(float interestRate) {
    this.interestRate = interestRate;
  }


  @Override
  public String toString() {
    DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(2);

    return super.toString() + ", Habenzins: " + df.format(interestRate);
  }

}
