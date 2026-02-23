package de.raywo.banking.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {

  private final String iban;
  private BigDecimal balance;
  private float interestRate;
  private Customer owner;
  private AccountStatus status;


  public Account(String iban, Customer owner) {
    this.iban = iban;
    this.owner = owner;
    this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    this.interestRate = 0.0f;
    this.status = AccountStatus.ACTIVE;
  }


  public String getIban() {
    return iban;
  }


  public BigDecimal getBalance() {
    return balance;
  }


  public float getInterestRate() {
    return interestRate;
  }


  public void setInterestRate(float interestRate) {
    this.interestRate = interestRate;
  }


  public Customer getOwner() {
    return owner;
  }


  public void setOwner(Customer owner) {
    this.owner = owner;
  }


  public AccountStatus getStatus() {
    return status;
  }


  public void setStatus(AccountStatus status) {
    this.status = status;
  }


  public void deposit(BigDecimal amount) {
    this.balance = balance.add(amount)
        .setScale(2, RoundingMode.HALF_UP);
  }


  public void withdraw(BigDecimal amount) {
    this.balance = balance.subtract(amount)
        .setScale(2, RoundingMode.HALF_UP);
  }


  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Account account = (Account) o;
    return iban.equals(account.iban);
  }


  @Override
  public int hashCode() {
    return iban.hashCode();
  }


  @Override
  public String toString() {
    return "[" + iban + "]" +
        ", Saldo: " + balance + "€ " +
        ", Zinssatz: " + interestRate + "%" +
        ", Inhaber: " + owner +
        ", (" + status + ")";
  }

}
