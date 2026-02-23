package de.raywo.banking;

import de.raywo.banking.domain.Account;
import de.raywo.banking.domain.Customer;
import de.raywo.banking.domain.InsufficentFundsException;
import de.raywo.banking.domain.InvalidAmountException;
import de.raywo.banking.system.SiBank;

import java.math.BigDecimal;

public class Main {

  public static void main(String[] args) {
    SiBank bank = new SiBank("Signal Iduna Bank", "Hamburg", "SIBAHH26");

    Customer ottokar = new Customer("Ottokar Domma", "Leipzig");
    Customer lieselotte = new Customer("Lieselotte Scharfsinnig", "Hamburg");

    Account acc1 = new Account("DE231234", lieselotte);
    Account acc2 = new Account("DE129087", ottokar);

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

      acc1.withdraw(BigDecimal.valueOf(10));
      System.out.println("Abhebung erfolgreich. Neuer Saldo: " + acc1.getBalance());

    } catch (InvalidAmountException exc) {
      System.err.println(exc.getMessage());
    } catch (InsufficentFundsException e) {
      System.err.println(e.getMessage());
    }

    System.out.println("-------------------");
    System.out.println("Konten");

    for (Account account : bank.getAccounts()) {
      System.out.println(account);
    }
  }

}

