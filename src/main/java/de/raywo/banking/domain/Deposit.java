package de.raywo.banking.domain;

import java.time.Instant;

public final class Deposit extends Transaction {

  public Deposit(String iban, String purpose, Money amount) {
    super(iban, purpose, amount);
  }


  public Deposit(String iban, String purpose, Money amount, Instant timestamp) {
    super(iban, purpose, amount, timestamp);
  }


  @Override
  void applyTo(Account account) {
    account.deposit(getAmount());
  }


  @Override
  String getSymbol() {
    return "+";
  }

}

