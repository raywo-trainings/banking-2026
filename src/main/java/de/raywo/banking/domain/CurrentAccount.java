package de.raywo.banking.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class CurrentAccount extends Account {

  private float interestRate;
  private BigDecimal limit;


  public CurrentAccount(String iban, Customer owner) {
    super(iban, owner);
    this.interestRate = 0.00f;
    this.limit = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
  }


  @Override
  public float getInterestRate() {
    return interestRate;
  }


  @Override
  public void setInterestRate(float interestRate) {
    this.interestRate = interestRate;
  }


  public BigDecimal getLimit() {
    return limit;
  }


  public void setLimit(BigDecimal limit) {
    this.limit = limit.setScale(2, RoundingMode.HALF_UP);
  }


  @Override
  public String toString() {
    return super.toString() +
        ", Dispo: " + limit + "€" +
        ", Sollzins: " + interestRate + "%";
  }


  @Override
  protected boolean isAmountAvailable(BigDecimal amount) {
    return amount.compareTo(getBalance().add(limit)) <= 0;
  }

}
