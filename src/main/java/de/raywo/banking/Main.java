package de.raywo.banking;

import de.raywo.banking.domain.*;
import de.raywo.banking.system.SiBank;
import de.raywo.banking.ui.BankingCLI;

import java.math.BigDecimal;

public class Main {

  public static void main(String[] args) {
    SiBank bank = new SiBank("Signal Iduna Bank", "Hamburg", "SIBAHH26");

    addTestData(bank);

    BankingCLI cli = new BankingCLI(bank);
    cli.start();
  }


  private static void addTestData(SiBank bank) {
    Customer ottokar = new Customer("Ottokar Domma", "Leipzig");
    Customer lieselotte = new Customer("Lieselotte Scharfsinnig", "Hamburg");

    bank.addCustomer(ottokar);
    bank.addCustomer(lieselotte);

    CurrentAccount giro = new CurrentAccount("DE231234", lieselotte);
    giro.setLimit(Money.euroOf(BigDecimal.valueOf(500)));
    bank.addAccount(giro);

    SavingsAccount spar = new SavingsAccount("DE129087", ottokar);
    spar.setInterestRate(0.015f);
    bank.addAccount(spar);

    try {
      giro.makeTransaction(new Deposit(giro.getIban(), "Gehaltseingang", Money.euroOf(BigDecimal.valueOf(2500))));
      spar.makeTransaction(new Deposit(spar.getIban(), "Spareinlage", Money.euroOf(BigDecimal.valueOf(1000))));
    } catch (Exception e) {
      System.err.println("Fehler beim Anlegen der Testdaten: " + e.getMessage());
    }
  }

}
