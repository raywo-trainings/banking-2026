package de.raywo.banking.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Girokonto – eine konkrete Unterklasse von {@link Account}.
 *
 * <p>Erweitert die Basisklasse um einen Dispositionskredit ({@code limit}) und
 * einen Sollzinssatz ({@code debitInterestRate}). Der Konstruktor ruft mit
 * {@code super(iban, owner)} den Konstruktor der Basisklasse auf, um die
 * gemeinsamen Felder zu initialisieren.</p>
 *
 * <p><b>Polymorphie:</b> Überschreibt {@link #isAmountAvailable(Money)}, um bei
 * der Verfügbarkeitsprüfung den Dispositionskredit zu berücksichtigen. Dadurch
 * kann {@link Account#withdraw(Money)} für Girokonten mehr Geld auszahlen als
 * der reine Saldo hergibt – ohne dass die {@code withdraw}-Methode den konkreten
 * Kontotyp kennen muss.</p>
 *
 * <p><b>Konzepte:</b> Vererbung ({@code extends}), Konstruktorverkettung
 * ({@code super(...)}), Polymorphie (Methoden-Überschreibung mit {@code @Override})</p>
 *
 * @see Account
 * @see SavingsAccount
 */
public class CurrentAccount extends Account {

  private float debitInterestRate;
  private Money limit;


  /**
   * Erzeugt ein neues Girokonto. Ruft {@code super(iban, owner)} auf, um die
   * gemeinsamen Felder der Basisklasse zu initialisieren.
   *
   * @param iban  die IBAN des Kontos
   * @param owner der Kontoinhaber
   */
  public CurrentAccount(String iban, Customer owner) {
    super(iban, owner);
    this.debitInterestRate = 0.00f;
    this.limit = Money.zeroEuro();
  }


  public float getDebitInterestRate() {
    return debitInterestRate;
  }


  public void setDebitInterestRate(float debitInterestRate) {
    this.debitInterestRate = debitInterestRate;
  }


  public Money getLimit() {
    return limit;
  }


  public void setLimit(Money limit) {
    this.limit = limit;
  }


  @Override
  public String toString() {
    DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(2);

    return super.toString() +
        ", Dispo: " + limit +
        ", Sollzins: " + df.format(debitInterestRate);
  }


  /**
   * Überschreibt die Verfügbarkeitsprüfung der Basisklasse, um den
   * Dispositionskredit einzubeziehen: Verfügbar ist Saldo + Dispo-Limit.
   *
   * <p>Dies ist ein Beispiel für <b>Polymorphie</b>: {@link Account#withdraw(Money)}
   * ruft {@code isAmountAvailable()} auf und erhält je nach Kontotyp zur Laufzeit
   * die passende Implementierung.</p>
   *
   * @param amount der zu prüfende Betrag
   * @return {@code true}, wenn der Betrag innerhalb von Saldo + Limit liegt
   * @throws CurrencyMismatchException falls die Währungen nicht übereinstimmen
   */
  @Override
  protected boolean isAmountAvailable(Money amount) throws CurrencyMismatchException {
    return amount.amount()
        .compareTo(getBalance().add(limit).amount()) <= 0;
  }

}
