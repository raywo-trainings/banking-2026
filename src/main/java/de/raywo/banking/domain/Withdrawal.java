package de.raywo.banking.domain;

import java.time.Instant;

/**
 * Abhebung – eine konkrete, als {@code final} deklarierte Unterklasse von
 * {@link Transaction}.
 *
 * <p>Das Schlüsselwort {@code final} ist hier durch die Sealed-Class-Hierarchie
 * erforderlich: Erlaubte Unterklassen einer {@code sealed} Klasse müssen als
 * {@code final}, {@code sealed} oder {@code non-sealed} deklariert werden.</p>
 *
 * <p>Die Methode {@link #applyTo(Account)} implementiert die konkrete Logik
 * einer Abhebung, indem sie {@link Account#withdraw(Money)} aufruft. Dabei
 * kann eine {@link InsufficientFundsException} auftreten, wenn das Guthaben
 * nicht ausreicht.</p>
 *
 * <p><b>Konzepte:</b> Sealed Classes, final-Klassen, Polymorphie,
 * Checked Exceptions</p>
 *
 * @see Deposit
 * @see Transaction
 */
public final class Withdrawal extends Transaction {

  public Withdrawal(String iban, String purpose, Money amount) {
    super(iban, purpose, amount);
  }


  public Withdrawal(String iban, String purpose, Money amount, Instant timestamp) {
    super(iban, purpose, amount, timestamp);
  }


  /**
   * Hebt den Transaktionsbetrag vom Konto ab.
   *
   * @throws InsufficientFundsException falls das verfügbare Guthaben nicht ausreicht
   */
  @Override
  void applyTo(Account account) throws InsufficientFundsException {
    account.withdraw(getAmount());
  }


  @Override
  String getSymbol() {
    return "-";
  }

}
