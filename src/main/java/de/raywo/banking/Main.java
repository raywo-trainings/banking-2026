package de.raywo.banking;

import de.raywo.banking.domain.Account;
import de.raywo.banking.domain.Customer;
import de.raywo.banking.system.SiBank;

import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    SiBank.initialize("Signal Iduna Bank", "Hamburg", "SIBAHH26");
    SiBank bank = SiBank.getInstance();

    if (bank.getAccounts().isEmpty()) {
      DataInitializer.initializeData(bank);
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

}

