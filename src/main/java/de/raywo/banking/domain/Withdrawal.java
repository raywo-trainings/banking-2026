package de.raywo.banking.domain;

import java.time.Instant;

public final class Withdrawal extends Transaction {

  public Withdrawal(String iban, String purpose, Money amount) {
    super(iban, purpose, amount);
  }


  public Withdrawal(String iban, String purpose, Money amount, Instant timestamp) {
    super(iban, purpose, amount, timestamp);
  }


  @Override
  void applyTo(Account account) throws InsufficientFundsException {
    account.withdraw(getAmount());
  }


  @Override
  String getSymbol() {
    return "-";
  }

}
