package de.raywo.banking.domain;

/**
 * Checked Exception: Wird geworfen, wenn die IBAN einer Transaktion nicht
 * zur IBAN des Kontos passt, auf das sie angewendet werden soll.
 *
 * <p>Als Checked Exception (erbt von {@link Exception}) muss dieser
 * Fehlerfall vom Aufrufer explizit behandelt werden. Das ist sinnvoll,
 * weil eine falsche Zuordnung von Transaktion zu Konto eine erwartbare
 * Fehlersituation darstellt.</p>
 *
 * <p><b>Konzept:</b> Checked vs. Unchecked Exceptions</p>
 *
 * @see InsufficientFundsException weitere Checked Exception im Projekt
 */
public class AccountMismatchException extends Exception {
  public AccountMismatchException(String message) {
    super(message);
  }
}
