package de.raywo.banking;

import de.raywo.banking.domain.*;
import de.raywo.banking.system.SiBank;

import java.math.BigDecimal;

public class Main {

  public static void main(String[] args) {
    SiBank bank = new SiBank("Signal Iduna Bank", "Hamburg", "SIBAHH26");

    Customer ottokar = new Customer("Ottokar Domma", "Leipzig");
    Customer lieselotte = new Customer("Lieselotte Scharfsinnig", "Hamburg");

    Account acc1 = new CurrentAccount("DE231234", lieselotte);
    Account acc2 = new CurrentAccount("DE129087", ottokar);

    bank.addCustomer(ottokar);
    bank.addCustomer(lieselotte);
    bank.addAccount(acc1);
    bank.addAccount(acc2);

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

    System.out.println("100€ einzahlen");
    try {
      acc1.deposit(BigDecimal.valueOf(100));
      System.out.println("Einzahlung erfolgreich. Neuer Saldo: " + acc1.getBalance() + "€");

       ((CurrentAccount) acc1).setLimit(BigDecimal.valueOf(100));
      acc1.withdraw(BigDecimal.valueOf(200));
      System.out.println("Abhebung erfolgreich. Neuer Saldo: " + acc1.getBalance());

    } catch (InvalidAmountException | InsufficentFundsException exc) {
      System.err.println(exc.getMessage());
    }

    System.out.println("-------------------");
    System.out.println("Konten");

    for (Account account : bank.getAccounts()) {
      System.out.println(account);
    }
  }

}

