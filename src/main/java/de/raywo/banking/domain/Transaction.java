package de.raywo.banking.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

sealed public abstract class Transaction permits Deposit, Withdrawal {
  private final String iban;
  private final String purpose;
  private final BigDecimal amount;
  private final Instant timestamp;


  public Transaction(String iban, String purpose, BigDecimal amount) {
    this(iban, purpose, amount, Instant.now());
  }


  public Transaction(String iban, String purpose, BigDecimal amount, Instant timestamp) {
    this.iban = iban;
    this.purpose = purpose;
    this.amount = amount;
    this.timestamp = timestamp;
  }


  public String getIban() {
    return iban;
  }


  public String getPurpose() {
    return purpose;
  }


  public BigDecimal getAmount() {
    return amount;
  }


  public Instant getTimestamp() {
    return timestamp;
  }


  abstract void applyTo(Account account) throws InsufficentFundsException;

  abstract String getSymbol();


  @Override
  public String toString() {
    LocalDateTime ldt = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
    String formattedTimestamp = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.MEDIUM).format(ldt);

    return formattedTimestamp +
        ": (" + getSymbol() + ") " +
        amount + "€, VWZ: " +
        purpose;
  }
}
