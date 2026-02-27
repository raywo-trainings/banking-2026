package de.raywo.banking.domain;

public class InsufficientFundsException extends Exception {

  public InsufficientFundsException(String message) {
    super(message);
  }

}
