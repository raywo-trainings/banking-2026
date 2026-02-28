package de.raywo.banking.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Abstrakte Basisklasse für Kontotransaktionen, implementiert als <b>Sealed Class</b>.
 *
 * <p>Das Schlüsselwort {@code sealed} (seit Java 17) zusammen mit {@code permits}
 * schränkt ein, welche Klassen von {@code Transaction} erben dürfen. Damit ist
 * garantiert, dass es nur {@link Deposit} und {@link Withdrawal} als
 * Transaktionstypen gibt. Die erlaubten Unterklassen müssen als {@code final},
 * {@code sealed} oder {@code non-sealed} deklariert werden – hier sind beide
 * {@code final}.</p>
 *
 * <p><b>Polymorphie:</b> Die abstrakte Methode {@link #applyTo(Account)} wird von
 * den konkreten Unterklassen implementiert. {@link Account#makeTransaction(Transaction)}
 * ruft lediglich {@code transaction.applyTo(this)} auf und überlässt es der konkreten
 * Transaktion, ob eingezahlt oder abgehoben wird.</p>
 *
 * <p>Alle Felder sind {@code final} – eine Transaktion ist nach der Erzeugung
 * unveränderlich.</p>
 *
 * <p><b>Konzepte:</b> Sealed Classes, abstrakte Klassen, Polymorphie,
 * Unveränderlichkeit, Serialisierung</p>
 *
 * @see Deposit
 * @see Withdrawal
 */
sealed public abstract class Transaction implements Serializable
    permits Deposit, Withdrawal {
  private final String iban;
  private final String purpose;
  private final Money amount;
  private final Instant timestamp;


  /**
   * Erzeugt eine neue Transaktion mit dem aktuellen Zeitstempel.
   *
   * @param iban    die IBAN des zugehörigen Kontos
   * @param purpose der Verwendungszweck
   * @param amount  der Transaktionsbetrag
   */
  public Transaction(String iban, String purpose, Money amount) {
    this(iban, purpose, amount, Instant.now());
  }


  /**
   * Erzeugt eine neue Transaktion mit explizitem Zeitstempel.
   *
   * @param iban      die IBAN des zugehörigen Kontos
   * @param purpose   der Verwendungszweck
   * @param amount    der Transaktionsbetrag
   * @param timestamp der Zeitpunkt der Transaktion
   */
  public Transaction(String iban, String purpose, Money amount, Instant timestamp) {
    this.iban = iban;
    this.purpose = purpose;
    this.amount = amount;
    this.timestamp = timestamp;
  }


  public String getIban() {
    return iban;
  }


  public String getPurpose() {
    return purpose;
  }


  public Money getAmount() {
    return amount;
  }


  public Instant getTimestamp() {
    return timestamp;
  }


  /**
   * Wendet diese Transaktion auf das übergebene Konto an. Die konkrete
   * Implementierung entscheidet, ob eingezahlt ({@link Deposit}) oder
   * abgehoben ({@link Withdrawal}) wird – ein Beispiel für <b>Polymorphie</b>.
   *
   * @param account das Konto, auf das die Transaktion angewendet wird
   * @throws InsufficientFundsException falls bei einer Abhebung das Guthaben
   *                                     nicht ausreicht
   */
  abstract void applyTo(Account account) throws InsufficientFundsException;

  /**
   * Gibt ein Zeichen zurück, das den Transaktionstyp kennzeichnet
   * ({@code "+"} für Einzahlung, {@code "-"} für Abhebung).
   *
   * @return das Symbol dieser Transaktion
   */
  abstract String getSymbol();


  @Override
  public String toString() {
    ZonedDateTime ldt = ZonedDateTime.ofInstant(timestamp, ZoneId.systemDefault());
    String formattedTimestamp = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.MEDIUM)
        .format(ldt);

    return formattedTimestamp +
        ": (" + getSymbol() + ") " +
        amount + ", VWZ: " +
        purpose;
  }
}
