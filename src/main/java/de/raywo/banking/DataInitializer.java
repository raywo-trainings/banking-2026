package de.raywo.banking;

import de.raywo.banking.domain.*;
import de.raywo.banking.system.SiBank;

import java.math.BigDecimal;

/**
 * Erzeugt Beispieldaten (Kunden, Konten, Transaktionen) für den ersten
 * Programmstart.
 *
 * <p>Diese Klasse wurde aus {@link Main} extrahiert, um die Verantwortlichkeiten
 * sauber zu trennen: {@code Main} kümmert sich um den Programmstart,
 * {@code DataInitializer} um die Erzeugung von Testdaten. Das entspricht dem
 * <em>Single-Responsibility-Prinzip</em> – jede Klasse hat genau eine Aufgabe.</p>
 *
 * <p>Die Methode {@link #initializeData(SiBank)} zeigt im Zusammenspiel mehrere
 * Konzepte: Erzeugen von Objekten ({@code new}), Polymorphie bei Transaktionen
 * ({@link de.raywo.banking.domain.Deposit} und {@link de.raywo.banking.domain.Withdrawal}),
 * Factory Methods ({@link de.raywo.banking.domain.Money#euroOf}), sowie die
 * Behandlung mehrerer Checked Exceptions in einem {@code catch}-Block über
 * Multi-Catch ({@code catch (A | B | C e)}).</p>
 *
 * @see Main
 * @see MainUI
 */
public class DataInitializer {

  /**
   * Befüllt die Bank mit Beispielkunden, -konten und -transaktionen.
   *
   * <p>Wird nur aufgerufen, wenn noch keine Konten vorhanden sind (erster Start).
   * Der {@code try-catch}-Block demonstriert <b>Multi-Catch</b> (seit Java 7):
   * Mehrere Exception-Typen können mit {@code |} in einem einzigen
   * {@code catch}-Block behandelt werden, wenn die Fehlerbehandlung identisch ist.</p>
   *
   * @param bank die {@link SiBank}-Instanz, in die die Daten eingefügt werden
   */
  public static void initializeData(SiBank bank) {
    System.out.println("Initialisiere Daten...");

    Customer ottokar = new Customer("Ottokar Domma", "Leipzig");
    Customer lieselotte = new Customer("Lieselotte Scharfsinnig", "Hamburg");

    Account acc1 = new CurrentAccount("DE231234", lieselotte);
    Account acc2 = new CurrentAccount("DE129087", ottokar);

    Transaction deposit1 = new Deposit(
        acc1.getIban(),
        "Einzahlung",
        Money.euroOf(BigDecimal.valueOf(100.45))
    );
    Transaction deposit2 = new Deposit(
        acc1.getIban(),
        "Einzahlung",
        Money.euroOf(BigDecimal.valueOf(200.45))
    );
    Transaction deposit3 = new Deposit(
        acc1.getIban(),
        "Einzahlung",
        Money.euroOf(BigDecimal.valueOf(300.45))
    );

    Transaction withdrawal1 = new Withdrawal(
        acc1.getIban(),
        "Auszahlung",
        Money.euroOf(BigDecimal.valueOf(150.00))
    );


    bank.addCustomer(ottokar);
    bank.addCustomer(lieselotte);
    bank.addAccount(acc1);
    bank.addAccount(acc2);

    try {
      System.out.println("Starte Einzahlungen:");
      acc1.makeTransaction(deposit1);
      acc1.makeTransaction(deposit2);
      acc1.makeTransaction(deposit3);
      System.out.println("Einzahlungen erfolgreich. Neuer Saldo: " + acc1.getBalance());

      acc1.makeTransaction(withdrawal1);
      System.out.println("Auszahlung erfolgreich. Neuer Saldo: " + acc1.getBalance());
      acc1.getTransactions().forEach(t -> System.out.println(t.toString()));

      ((CurrentAccount) acc1).setLimit(Money.euroOf(BigDecimal.valueOf(1000.00)));

    } catch (InsufficientFundsException | AccountMismatchException | InactiveAccountException | CurrencyMismatchException exc) {
      System.err.println(exc.getMessage());
    }

    System.out.println("Daten erfolgreich initialisiert.");
  }

}

