package de.raywo.banking.domain;

public class AccountMismatchException extends Exception {
  public AccountMismatchException(String message) {
    super(message);
  }
}
