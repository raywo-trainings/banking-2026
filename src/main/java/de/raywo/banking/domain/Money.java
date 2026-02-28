package de.raywo.banking.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Currency;
import java.util.Objects;

/**
 * Ein Value Object, das einen Geldbetrag mit zugehöriger Währung repräsentiert.
 *
 * <p>{@code Money} ist als <b>Record</b> (seit Java 14) implementiert. Der Compiler
 * generiert automatisch Konstruktor, Getter ({@code amount()}, {@code currency()}),
 * {@code equals}, {@code hashCode} und {@code toString}. Der <b>Compact Constructor</b>
 * (ohne Parameterliste) ermöglicht Validierung und Normalisierung der Werte direkt
 * bei der Erzeugung – hier wird sichergestellt, dass kein Feld {@code null} ist und
 * der Betrag immer auf zwei Nachkommastellen gerundet wird.</p>
 *
 * <p>Als Value Object wird {@code Money} durch seine Werte definiert, nicht durch
 * eine Identität: Zwei {@code Money}-Objekte mit gleichem Betrag und gleicher Währung
 * sind immer gleich, unabhängig davon, wann oder wo sie erzeugt wurden.</p>
 *
 * <p>Die statischen Methoden {@link #euroOf(BigDecimal)} und {@link #zeroEuro()} sind
 * <b>Factory Methods</b>, die das Erzeugen von {@code Money}-Objekten vereinfachen
 * und die Absicht des Codes lesbarer machen.</p>
 *
 * <p>Rechenoperationen wie {@link #add(Money)} und {@link #subtract(Money)} erzeugen
 * stets ein neues {@code Money}-Objekt (Unveränderlichkeit). Vor jeder Operation
 * wird geprüft, ob die Währungen übereinstimmen – andernfalls wird eine
 * {@link CurrencyMismatchException} geworfen.</p>
 *
 * <p><b>Konzepte:</b> Records, Value Objects, Compact Constructor, Factory Methods,
 * Unveränderlichkeit</p>
 *
 * @param amount   der Geldbetrag, wird automatisch auf 2 Nachkommastellen gerundet
 * @param currency die Währung des Betrags
 */
public record Money(
    BigDecimal amount,
    Currency currency
) implements Serializable {

  /**
   * Compact Constructor – wird bei jeder Erzeugung eines {@code Money}-Objekts
   * aufgerufen. Validiert, dass keine Werte {@code null} sind, und normalisiert
   * den Betrag auf zwei Nachkommastellen.
   */
  public Money {
    Objects.requireNonNull(amount);
    Objects.requireNonNull(currency);
    amount = amount.setScale(2, RoundingMode.HALF_UP);
  }


  /**
   * Factory Method: Erzeugt ein {@code Money}-Objekt in Euro mit dem
   * angegebenen Betrag.
   *
   * @param amount der Geldbetrag
   * @return ein neues {@code Money}-Objekt mit Währung EUR
   */
  public static Money euroOf(BigDecimal amount) {
    return new Money(amount, Currency.getInstance("EUR"));
  }


  /**
   * Factory Method: Erzeugt ein {@code Money}-Objekt mit 0,00 EUR.
   *
   * @return ein neues {@code Money}-Objekt mit Betrag 0 und Währung EUR
   */
  public static Money zeroEuro() {
    return euroOf(BigDecimal.ZERO);
  }


  /**
   * Addiert einen anderen {@code Money}-Betrag und gibt ein neues Objekt zurück.
   * Das aktuelle Objekt bleibt unverändert (Unveränderlichkeit).
   *
   * @param other der zu addierende Betrag (muss dieselbe Währung haben)
   * @return ein neues {@code Money}-Objekt mit dem Summenbetrag
   * @throws CurrencyMismatchException falls die Währungen nicht übereinstimmen
   */
  public Money add(Money other) {
    Objects.requireNonNull(other);
    requireCurrency(other, this.currency());

    return new Money(amount.add(other.amount), currency);
  }


  /**
   * Subtrahiert einen anderen {@code Money}-Betrag und gibt ein neues Objekt zurück.
   * Das aktuelle Objekt bleibt unverändert (Unveränderlichkeit).
   *
   * @param other der abzuziehende Betrag (muss dieselbe Währung haben)
   * @return ein neues {@code Money}-Objekt mit dem Differenzbetrag
   * @throws CurrencyMismatchException falls die Währungen nicht übereinstimmen
   */
  public Money subtract(Money other) {
    Objects.requireNonNull(other);
    requireCurrency(other, this.currency());

    return new Money(amount.subtract(other.amount), currency);
  }


  /**
   * Erzeugt einen {@link Comparator}, der zwei {@code Money}-Objekte anhand
   * ihres Betrags vergleicht. Beide Objekte müssen die angegebene Währung haben.
   *
   * @param expected die erwartete Währung beider zu vergleichender Objekte
   * @return einen Comparator für {@code Money}-Objekte gleicher Währung
   * @throws CurrencyMismatchException falls eines der verglichenen Objekte eine
   *                                    andere Währung hat
   */
  public static Comparator<Money> sameCurrencyComparator(Currency expected) {
    Objects.requireNonNull(expected);

    return (a, b) -> {
      requireCurrency(a, expected);
      requireCurrency(b, expected);

      return a.amount.compareTo(b.amount);
    };
  }


  @Override
  public String toString() {
    NumberFormat df = NumberFormat.getCurrencyInstance();
    df.setCurrency(currency());

    return df.format(amount);
  }


  private static void requireCurrency(Money m, Currency expected) {
    if (!m.currency.equals(expected)) {
      throw new CurrencyMismatchException("Unerwartete Währung: " + m.currency
          + " (erwartet: " + expected + ")");
    }
  }

}
