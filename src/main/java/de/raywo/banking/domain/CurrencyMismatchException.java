package de.raywo.banking.domain;

public class CurrencyMismatchException extends RuntimeException {
  public CurrencyMismatchException(String message) {
    super(message);
  }
}
