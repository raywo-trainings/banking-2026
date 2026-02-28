package de.raywo.banking;

import de.raywo.banking.system.SiBank;
import de.raywo.banking.ui.BankingCLI;

public class MainUI {

  public static void main(String[] args) {
    SiBank.initialize("Signal Iduna Bank", "Hamburg", "SIBAHH26");
    SiBank bank = SiBank.getInstance();

    if (bank.getAccounts().isEmpty()) {
      DataInitializer.initializeData(bank);
    }

    new BankingCLI(bank).start();
  }

}
