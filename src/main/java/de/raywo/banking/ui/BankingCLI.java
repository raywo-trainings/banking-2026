package de.raywo.banking.ui;

import de.raywo.banking.system.SiBank;

import java.math.BigDecimal;
import java.util.Scanner;

public class BankingCLI {

  private final SiBank bank;
  private final Scanner scanner;
  private final CustomerMenu customerMenu;
  private final AccountMenu accountMenu;
  private final TransactionMenu transactionMenu;


  public BankingCLI(SiBank bank) {
    this.bank = bank;
    this.scanner = new Scanner(System.in);
    this.customerMenu = new CustomerMenu(bank, scanner);
    this.accountMenu = new AccountMenu(bank, scanner);
    this.transactionMenu = new TransactionMenu(bank, scanner);
  }


  public void start() {
    System.out.println("Willkommen bei der " + bank.getName() + "!");
    System.out.println();

    boolean running = true;
    while (running) {
      printMainMenu();
      int choice = readInt("Ihre Wahl: ");

      switch (choice) {
        case 1 -> customerMenu.show();
        case 2 -> accountMenu.show();
        case 3 -> transactionMenu.showDeposit();
        case 4 -> transactionMenu.showWithdrawal();
        case 0 -> running = false;
        default -> System.out.println("Ungültige Auswahl. Bitte versuchen Sie es erneut.");
      }

      System.out.println();
    }

    System.out.println("Auf Wiedersehen!");
    scanner.close();
  }


  private void printMainMenu() {
    System.out.println("=== " + bank.getName() + " – Hauptmenü ===");
    System.out.println("1. Kundenverwaltung");
    System.out.println("2. Kontoverwaltung");
    System.out.println("3. Einzahlung");
    System.out.println("4. Auszahlung");
    System.out.println("0. Beenden");
    System.out.println();
  }


  static int readInt(String prompt, Scanner scanner) {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine().trim();
      try {
        return Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Bitte geben Sie eine gültige Zahl ein.");
      }
    }
  }


  private int readInt(String prompt) {
    return readInt(prompt, scanner);
  }


  static String readLine(String prompt, Scanner scanner) {
    System.out.print(prompt);
    return scanner.nextLine().trim();
  }


  static BigDecimal readBigDecimal(String prompt, Scanner scanner) {
    while (true) {
      System.out.print(prompt);
      String input = scanner.nextLine().trim().replace(",", ".");
      try {
        BigDecimal value = new BigDecimal(input);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
          System.out.println("Der Betrag muss größer als 0 sein.");
          continue;
        }
        return value;
      } catch (NumberFormatException e) {
        System.out.println("Bitte geben Sie einen gültigen Betrag ein.");
      }
    }
  }

}
