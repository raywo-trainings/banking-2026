package de.raywo.banking.domain;

public class InsufficentFundsException extends RuntimeException {

  public InsufficentFundsException(String message) {
    super(message);
  }

}
