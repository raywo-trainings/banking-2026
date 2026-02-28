package de.raywo.banking.ui;

import de.raywo.banking.domain.Customer;
import de.raywo.banking.system.SiBank;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static de.raywo.banking.ui.AccountMenu.getCustomer;

public class CustomerMenu {

  private final SiBank bank;
  private final Scanner scanner;


  public CustomerMenu(SiBank bank, Scanner scanner) {
    this.bank = bank;
    this.scanner = scanner;
  }


  public void show() {
    boolean running = true;
    while (running) {
      printMenu();
      int choice = BankingCLI.readInt("Ihre Wahl: ", scanner);

      switch (choice) {
        case 1 -> listCustomers();
        case 2 -> createCustomer();
        case 3 -> editCustomer();
        case 4 -> deleteCustomer();
        case 0 -> running = false;
        default -> System.out.println("Ungültige Auswahl. Bitte versuchen Sie es erneut.");
      }

      System.out.println();
    }
  }


  private void printMenu() {
    System.out.println("=== Kundenverwaltung ===");
    System.out.println("1. Alle Kunden anzeigen");
    System.out.println("2. Neuen Kunden anlegen");
    System.out.println("3. Kunden bearbeiten");
    System.out.println("4. Kunden löschen");
    System.out.println("0. Zurück");
    System.out.println();
  }


  private void listCustomers() {
    List<Customer> customers = new ArrayList<>(bank.getCustomers());

    if (customers.isEmpty()) {
      System.out.println("Keine Kunden vorhanden.");
      return;
    }

    System.out.println("--- Kundenliste ---");
    for (int i = 0; i < customers.size(); i++) {
      System.out.println((i + 1) + ". " + customers.get(i));
    }
  }


  private void createCustomer() {
    System.out.println("--- Neuen Kunden anlegen ---");
    String name = BankingCLI.readLine("Name: ", scanner);
    if (name.isEmpty()) {
      System.out.println("Name darf nicht leer sein. Vorgang abgebrochen.");
      return;
    }

    String city = BankingCLI.readLine("Stadt: ", scanner);
    if (city.isEmpty()) {
      System.out.println("Stadt darf nicht leer sein. Vorgang abgebrochen.");
      return;
    }

    Customer customer = new Customer(name, city);
    bank.addCustomer(customer);
    System.out.println("Kunde wurde angelegt: " + customer);
  }


  private void editCustomer() {
    Customer customer = selectCustomer("bearbeiten");
    if (customer == null) return;

    System.out.println("Aktuell: " + customer);

    String name = BankingCLI.readLine("Neuer Name (leer = beibehalten): ", scanner);
    if (!name.isEmpty()) {
      customer.setName(name);
    }

    String city = BankingCLI.readLine("Neue Stadt (leer = beibehalten): ", scanner);
    if (!city.isEmpty()) {
      customer.setCity(city);
    }

    System.out.println("Kunde wurde aktualisiert: " + customer);
  }


  private void deleteCustomer() {
    Customer customer = selectCustomer("löschen");
    if (customer == null) return;

    bank.removeCustomer(customer);
    System.out.println("Kunde wurde gelöscht: " + customer);
  }


  Customer selectCustomer(String action) {
    List<Customer> customers = new ArrayList<>(bank.getCustomers());

    if (customers.isEmpty()) {
      System.out.println("Keine Kunden vorhanden.");
      return null;
    }

    System.out.println("--- Kunden zum " + action + " auswählen ---");
    return getCustomer(customers, scanner);
  }

}
