package de.raywo.banking.domain;

import java.time.Instant;

/**
 * Einzahlung – eine konkrete, als {@code final} deklarierte Unterklasse von
 * {@link Transaction}.
 *
 * <p>Das Schlüsselwort {@code final} ist hier durch die Sealed-Class-Hierarchie
 * erforderlich: Erlaubte Unterklassen einer {@code sealed} Klasse müssen als
 * {@code final}, {@code sealed} oder {@code non-sealed} deklariert werden.</p>
 *
 * <p>Die Methode {@link #applyTo(Account)} implementiert die konkrete Logik
 * einer Einzahlung, indem sie {@link Account#deposit(Money)} aufruft.</p>
 *
 * <p><b>Konzepte:</b> Sealed Classes, final-Klassen, Polymorphie</p>
 *
 * @see Withdrawal
 * @see Transaction
 */
public final class Deposit extends Transaction {

  public Deposit(String iban, String purpose, Money amount) {
    super(iban, purpose, amount);
  }


  public Deposit(String iban, String purpose, Money amount, Instant timestamp) {
    super(iban, purpose, amount, timestamp);
  }


  /** Zahlt den Transaktionsbetrag auf das Konto ein. */
  @Override
  void applyTo(Account account) {
    account.deposit(getAmount());
  }


  @Override
  String getSymbol() {
    return "+";
  }

}

