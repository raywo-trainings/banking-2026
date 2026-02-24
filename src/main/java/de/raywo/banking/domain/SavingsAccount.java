package de.raywo.banking.domain;

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
    return super.toString() + ", Habenzins: " + interestRate + "%";
  }
}
