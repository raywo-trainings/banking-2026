package de.raywo.banking.ui;

import de.raywo.banking.domain.*;
import de.raywo.banking.system.SiBank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountMenu {

  private final SiBank bank;
  private final Scanner scanner;


  public AccountMenu(SiBank bank, Scanner scanner) {
    this.bank = bank;
    this.scanner = scanner;
  }


  public void show() {
    boolean running = true;
    while (running) {
      printMenu();
      int choice = BankingCLI.readInt("Ihre Wahl: ", scanner);

      switch (choice) {
        case 1 -> listAccounts();
        case 2 -> createCurrentAccount();
        case 3 -> createSavingsAccount();
        case 4 -> editAccount();
        case 5 -> deleteAccount();
        case 0 -> running = false;
        default -> System.out.println("Ungültige Auswahl. Bitte versuchen Sie es erneut.");
      }

      System.out.println();
    }
  }


  private void printMenu() {
    System.out.println("=== Kontoverwaltung ===");
    System.out.println("1. Alle Konten anzeigen");
    System.out.println("2. Neues Girokonto anlegen");
    System.out.println("3. Neues Sparkonto anlegen");
    System.out.println("4. Konto bearbeiten (Zinssatz, Dispo)");
    System.out.println("5. Konto löschen");
    System.out.println("0. Zurück");
    System.out.println();
  }


  private void listAccounts() {
    List<Account> accounts = new ArrayList<>(bank.getAccounts());

    if (accounts.isEmpty()) {
      System.out.println("Keine Konten vorhanden.");
      return;
    }

    System.out.println("--- Kontoliste ---");
    for (int i = 0; i < accounts.size(); i++) {
      System.out.println((i + 1) + ". " + accounts.get(i));
    }
  }


  private void createCurrentAccount() {
    System.out.println("--- Neues Girokonto anlegen ---");
    String iban = BankingCLI.readLine("IBAN: ", scanner);
    if (iban.isEmpty()) {
      System.out.println("IBAN darf nicht leer sein. Vorgang abgebrochen.");
      return;
    }

    Customer owner = selectCustomerForAccount();
    if (owner == null) return;

    CurrentAccount account = new CurrentAccount(iban, owner);
    bank.addAccount(account);
    System.out.println("Girokonto wurde angelegt: " + account);
  }


  private void createSavingsAccount() {
    System.out.println("--- Neues Sparkonto anlegen ---");
    String iban = BankingCLI.readLine("IBAN: ", scanner);
    if (iban.isEmpty()) {
      System.out.println("IBAN darf nicht leer sein. Vorgang abgebrochen.");
      return;
    }

    Customer owner = selectCustomerForAccount();
    if (owner == null) return;

    SavingsAccount account = new SavingsAccount(iban, owner);
    bank.addAccount(account);
    System.out.println("Sparkonto wurde angelegt: " + account);
  }


  private void editAccount() {
    Account account = selectAccount("bearbeiten");
    if (account == null) return;

    System.out.println("Aktuell: " + account);

    String rateInput = BankingCLI.readLine("Neuer Zinssatz in % (leer = beibehalten): ", scanner);
    if (!rateInput.isEmpty()) {
      try {
        float rate = Float.parseFloat(rateInput.replace(",", ".")) / 100f;
        if (account instanceof CurrentAccount currentAccount) {
          currentAccount.setDebitInterestRate(rate);
        } else if (account instanceof SavingsAccount savingsAccount) {
          savingsAccount.setCreditInterestRate(rate);
        }
      } catch (NumberFormatException e) {
        System.out.println("Ungültiger Zinssatz. Zinssatz wurde nicht geändert.");
      }
    }

    if (account instanceof CurrentAccount currentAccount) {
      String limitInput = BankingCLI.readLine("Neues Dispolimit in EUR (leer = beibehalten): ", scanner);
      if (!limitInput.isEmpty()) {
        try {
          BigDecimal limitValue = new BigDecimal(limitInput.replace(",", "."));
          currentAccount.setLimit(Money.euroOf(limitValue));
        } catch (NumberFormatException e) {
          System.out.println("Ungültiges Dispolimit. Limit wurde nicht geändert.");
        }
      }
    }

    System.out.println("Konto wurde aktualisiert: " + account);
  }


  private void deleteAccount() {
    Account account = selectAccount("löschen");
    if (account == null) return;

    bank.removeAccount(account.getIban());
    System.out.println("Konto wurde gelöscht: " + account);
  }


  Account selectAccount(String action) {
    List<Account> accounts = new ArrayList<>(bank.getAccounts());

    if (accounts.isEmpty()) {
      System.out.println("Keine Konten vorhanden.");
      return null;
    }

    System.out.println("--- Konto zum " + action + " auswählen ---");

    return getAccount(accounts, scanner);
  }


  static Account getAccount(List<Account> accounts, Scanner scanner) {
    for (int i = 0; i < accounts.size(); i++) {
      System.out.println((i + 1) + ". " + accounts.get(i));
    }

    int index = BankingCLI.readInt("Nummer: ", scanner);
    if (index < 1 || index > accounts.size()) {
      System.out.println("Ungültige Auswahl.");
      return null;
    }

    return accounts.get(index - 1);
  }


  static Customer getCustomer(List<Customer> customers, Scanner scanner) {
    for (int i = 0; i < customers.size(); i++) {
      System.out.println((i + 1) + ". " + customers.get(i));
    }

    int index = BankingCLI.readInt("Nummer: ", scanner);
    if (index < 1 || index > customers.size()) {
      System.out.println("Ungültige Auswahl.");
      return null;
    }

    return customers.get(index - 1);
  }


  private Customer selectCustomerForAccount() {
    List<Customer> customers = new ArrayList<>(bank.getCustomers());

    if (customers.isEmpty()) {
      System.out.println("Keine Kunden vorhanden. Bitte legen Sie zuerst einen Kunden an.");
      return null;
    }

    System.out.println("Kontoinhaber auswählen:");
    return getCustomer(customers, scanner);
  }

}
