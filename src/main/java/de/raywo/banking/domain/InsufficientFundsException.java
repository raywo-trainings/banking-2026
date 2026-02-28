package de.raywo.banking.domain;

/**
 * Checked Exception: Wird geworfen, wenn eine Abhebung das verfügbare
 * Guthaben (inkl. eventuellem Dispositionskredit) übersteigt.
 *
 * <p>Als Checked Exception (erbt von {@link Exception}) erzwingt der
 * Compiler, dass der Aufrufer diesen Fehlerfall behandelt – entweder mit
 * {@code try-catch} oder durch Weiterreichen mit {@code throws}. Das ist
 * hier sinnvoll, weil unzureichendes Guthaben eine erwartbare
 * Geschäftssituation ist, auf die das Programm reagieren muss.</p>
 *
 * <p><b>Konzept:</b> Checked vs. Unchecked Exceptions</p>
 *
 * @see CurrencyMismatchException Beispiel für eine Unchecked Exception
 */
public class InsufficientFundsException extends Exception {

  public InsufficientFundsException(String message) {
    super(message);
  }

}
