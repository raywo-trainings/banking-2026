package de.raywo.banking.domain;

import java.math.BigDecimal;
import java.time.Instant;

public final class Withdrawal extends Transaction {

  public Withdrawal(String iban, String purpose, BigDecimal amount) {
    super(iban, purpose, amount);
  }


  public Withdrawal(String iban, String purpose, BigDecimal amount, Instant timestamp) {
    super(iban, purpose, amount, timestamp);
  }


  @Override
  public void applyTo(Account account) throws InsufficentFundsException {
    account.withdraw(getAmount());
  }


  @Override
  String getSymbol() {
    return "-";
  }

}
