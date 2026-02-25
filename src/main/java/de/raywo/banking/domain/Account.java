package de.raywo.banking.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {

  private final String iban;
  private BigDecimal balance;
  private float interestRate;
  private Customer owner;
  private AccountStatus status;
  private final List<Transaction> transactions;


  public Account(String iban, Customer owner) {
    this.iban = iban;
    this.owner = owner;
    this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    this.interestRate = 0.0f;
    this.status = AccountStatus.ACTIVE;
    this.transactions = new ArrayList<>();
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


  public List<Transaction> getTransactions() {
    return transactions;
  }


  public void makeTransaction(Transaction transaction) throws InsufficentFundsException, AccountMismatchException {
    if (!transaction.getIban().equals(iban)) {
      throw new AccountMismatchException("Die IBAN der Transaktion passt nicht zur IBAN des Kontos.");
    }

    transaction.applyTo(this);
    this.transactions.add(transaction);
  }


  void deposit(BigDecimal amount) {
    this.balance = balance.add(amount)
        .setScale(2, RoundingMode.HALF_UP);
  }


  void withdraw(BigDecimal amount) throws InsufficentFundsException {
    if (!isAmountAvailable(amount)) {
      throw new InsufficentFundsException("Der abzuhebende Betrag übersteigt das verfügbare Guthaben.");
    }

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


  protected boolean isAmountAvailable(BigDecimal amount) {
    return amount.compareTo(balance) <= 0;
  }

}
