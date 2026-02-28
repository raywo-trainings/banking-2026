package de.raywo.banking.domain;

/**
 * Checked Exception: Wird geworfen, wenn eine Transaktion auf einem inaktiven
 * Konto durchgeführt werden soll.
 *
 * <p>Als Checked Exception (erbt von {@link Exception}) erzwingt der Compiler,
 * dass der Aufrufer diesen Fehlerfall behandelt. Ein inaktives Konto ist eine
 * erwartbare Geschäftssituation, auf die das Programm reagieren muss.</p>
 *
 * <p><b>Konzept:</b> Checked vs. Unchecked Exceptions</p>
 *
 * @see AccountStatus
 * @see Account#makeTransaction(Transaction)
 */
public class InactiveAccountException extends Exception {

  public InactiveAccountException(String message) {
    super(message);
  }

}
