package de.raywo.banking.domain;

public class InvalidAmountException extends Exception {
  public InvalidAmountException(String message) {
    super(message);
  }
}
