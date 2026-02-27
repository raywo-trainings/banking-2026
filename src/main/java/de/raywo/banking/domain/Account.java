package de.raywo.banking.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public abstract class Account implements Serializable, Identifiable<String> {

  private final String iban;
  private Money balance;
  private Customer owner;
  private AccountStatus status;
  private final List<Transaction> transactions;


  public Account(String iban, Customer owner) {
    Objects.requireNonNull(iban, "iban darf nicht null sein");
    Objects.requireNonNull(owner, "owner darf nicht null sein");

    this.iban = iban;
    this.owner = owner;
    this.balance = Money.zeroEuro();
    this.status = AccountStatus.ACTIVE;
    this.transactions = new ArrayList<>();
  }


  @Override
  public String getId() {
    return iban;
  }


  public String getIban() {
    return iban;
  }


  public Money getBalance() {
    return balance;
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
    return Collections.unmodifiableList(transactions);
  }


  public void makeTransaction(Transaction transaction) throws InsufficientFundsException, AccountMismatchException {
    if (!transaction.getIban().equals(iban)) {
      throw new AccountMismatchException("Die IBAN der Transaktion passt nicht zur IBAN des Kontos.");
    }

    transaction.applyTo(this);
    this.transactions.add(transaction);
  }


  void deposit(Money amount) {
    this.balance = this.balance.add(amount);
  }


  void withdraw(Money amount) throws InsufficientFundsException {
    if (!isAmountAvailable(amount)) {
      throw new InsufficientFundsException("Der abzuhebende Betrag übersteigt das verfügbare Guthaben.");
    }

    this.balance = balance.subtract(amount);
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
        ", Saldo: " + balance +
        ", Inhaber: " + owner +
        ", (" + status + ")";
  }


  protected boolean isAmountAvailable(Money amount) {
    Comparator<Money> comparator = Money.sameCurrencyComparator(balance.currency());

    return comparator.compare(amount, balance) <= 0;
  }

}
