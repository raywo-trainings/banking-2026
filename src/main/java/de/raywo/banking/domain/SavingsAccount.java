package de.raywo.banking.domain;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Sparkonto – eine konkrete Unterklasse von {@link Account}.
 *
 * <p>Erweitert die Basisklasse um einen Habenzinssatz ({@code creditInterestRate}).
 * Im Gegensatz zum {@link CurrentAccount} besitzt ein Sparkonto keinen
 * Dispositionskredit – es kann nur bis zum verfügbaren Saldo abgehoben werden
 * (die geerbte {@link Account#isAmountAvailable(Money)}-Methode wird nicht
 * überschrieben).</p>
 *
 * <p><b>Konzepte:</b> Vererbung ({@code extends}), Konstruktorverkettung
 * ({@code super(...)})</p>
 *
 * @see Account
 * @see CurrentAccount
 */
public class SavingsAccount extends Account {

  private float creditInterestRate;


  /**
   * Erzeugt ein neues Sparkonto. Ruft {@code super(iban, owner)} auf, um die
   * gemeinsamen Felder der Basisklasse zu initialisieren.
   *
   * @param iban  die IBAN des Kontos
   * @param owner der Kontoinhaber
   */
  public SavingsAccount(String iban, Customer owner) {
    super(iban, owner);
    this.creditInterestRate = 0.0f;
  }


  public float getCreditInterestRate() {
    return creditInterestRate;
  }


  public void setCreditInterestRate(float creditInterestRate) {
    this.creditInterestRate = creditInterestRate;
  }


  @Override
  public String toString() {
    DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance();
    df.setMaximumFractionDigits(2);
    df.setMinimumFractionDigits(2);

    return super.toString() + ", Habenzins: " + df.format(creditInterestRate);
  }

}
