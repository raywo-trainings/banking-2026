package de.raywo.banking.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CurrentAccount extends Account {

  private float debitInterestRate;
  private Money limit;


  public CurrentAccount(String iban, Customer owner) {
    super(iban, owner);
    this.debitInterestRate = 0.00f;
    this.limit = Money.zeroEuro();
  }


  public float getDebitInterestRate() {
    return debitInterestRate;
  }


  public void setDebitInterestRate(float debitInterestRate) {
    this.debitInterestRate = debitInterestRate;
  }


  public Money getLimit() {
    return limit;
  }


  public void setLimit(Money limit) {
    this.limit = limit;
  }


  @Override
  public String toString() {
    DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(2);

    return super.toString() +
        ", Dispo: " + limit +
        ", Sollzins: " + df.format(debitInterestRate);
  }


  @Override
  protected boolean isAmountAvailable(Money amount) throws CurrencyMismatchException {
    return amount.amount()
        .compareTo(getBalance().add(limit).amount()) <= 0;
  }

}
