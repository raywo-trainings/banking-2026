package de.raywo.banking.domain;

import java.math.BigDecimal;
import java.time.Instant;

public final class Deposit extends Transaction {

  public Deposit(String iban, String purpose, BigDecimal amount) {
    super(iban, purpose, amount);
  }


  public Deposit(String iban, String purpose, BigDecimal amount, Instant timestamp) {
    super(iban, purpose, amount, timestamp);
  }


  @Override
  public void applyTo(Account account) {
    account.deposit(getAmount());
  }


  @Override
  String getSymbol() {
    return "+";
  }

}

