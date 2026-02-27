package de.raywo.banking;

import de.raywo.banking.domain.*;
import de.raywo.banking.system.SiBank;

import java.io.IOException;
import java.math.BigDecimal;

public class Main {

  public static void main(String[] args) throws IOException {
    SiBank.initialize("Signal Iduna Bank", "Hamburg", "SIBAHH26");
    SiBank bank = SiBank.getInstance();

    if (bank.getAccounts().isEmpty()) {
      initializeData(bank);
    }

//    try {
//      Account someAccount = bank.getAccount("DE129087");
//      System.out.println("Gesuchtes Konto: " + someAccount);
//    } catch (NotFoundException e) {
//      System.err.println(e.getMessage());
//    }

    System.out.println("-------------------");
    System.out.println("Bankinformationen");
    System.out.println(bank);

    System.out.println("-------------------");
    System.out.println("Kundenliste");

    for (Customer customer : bank.getCustomers()) {
      System.out.println(customer);
    }

    System.out.println("-------------------");
    System.out.println("Konten");

    for (Account account : bank.getAccounts()) {
      System.out.println(account);
    }

    bank.persist();
  }


  private static void initializeData(SiBank bank) {
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

//       ((CurrentAccount) acc1).setLimit(BigDecimal.valueOf(100));
//      acc1.withdraw(BigDecimal.valueOf(200));
//      System.out.println("Abhebung erfolgreich. Neuer Saldo: " + acc1.getBalance());

    } catch (InsufficientFundsException | AccountMismatchException | CurrencyMismatchException exc) {
      System.err.println(exc.getMessage());
    }

    System.out.println("Daten erfolgreich initialisiert.");
  }

}

