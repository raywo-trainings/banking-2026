package de.raywo.banking.domain;

public class InsufficentFundsException extends Exception {

  public InsufficentFundsException(String message) {
    super(message);
  }

}
