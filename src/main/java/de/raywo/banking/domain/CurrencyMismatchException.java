package de.raywo.banking.domain;

/**
 * Unchecked Exception: Wird geworfen, wenn eine Rechenoperation auf
 * {@link Money}-Objekten mit unterschiedlichen Währungen versucht wird.
 *
 * <p>Als Unchecked Exception (erbt von {@link RuntimeException}) muss dieser
 * Fehler vom Aufrufer <em>nicht</em> explizit behandelt werden. Das ist hier
 * bewusst gewählt, weil ein Währungs-Mismatch auf einen Programmierfehler
 * hindeutet, der im Normalfall durch korrekte Verwendung des Codes
 * vermieden wird – anders als z.&nbsp;B. fehlendes Guthaben, das eine
 * reguläre Geschäftssituation ist.</p>
 *
 * <p><b>Konzept:</b> Checked vs. Unchecked Exceptions</p>
 *
 * @see InsufficientFundsException Beispiel für eine Checked Exception
 */
public class CurrencyMismatchException extends RuntimeException {
  public CurrencyMismatchException(String message) {
    super(message);
  }
}
