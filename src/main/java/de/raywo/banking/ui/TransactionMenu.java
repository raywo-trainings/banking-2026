package de.raywo.banking.ui;

import de.raywo.banking.domain.*;
import de.raywo.banking.system.SiBank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionMenu {

  private final SiBank bank;
  private final Scanner scanner;


  public TransactionMenu(SiBank bank, Scanner scanner) {
    this.bank = bank;
    this.scanner = scanner;
  }


  public void showDeposit() {
    System.out.println("=== Einzahlung ===");
    Account account = selectAccount();
    if (account == null) return;

    BigDecimal amount = BankingCLI.readBigDecimal("Betrag in EUR: ", scanner);
    String purpose = BankingCLI.readLine("Verwendungszweck: ", scanner);
    if (purpose.isEmpty()) {
      purpose = "Einzahlung";
    }

    Transaction deposit = new Deposit(
        account.getIban(),
        purpose,
        Money.euroOf(amount)
    );

    try {
      account.makeTransaction(deposit);
      System.out.println("Einzahlung erfolgreich. Neuer Saldo: " + account.getBalance());
    } catch (InsufficentFundsException | AccountMismatchException e) {
      System.out.println("Fehler: " + e.getMessage());
    }
  }


  public void showWithdrawal() {
    System.out.println("=== Auszahlung ===");
    Account account = selectAccount();
    if (account == null) return;

    System.out.println("Aktueller Saldo: " + account.getBalance());
    BigDecimal amount = BankingCLI.readBigDecimal("Betrag in EUR: ", scanner);
    String purpose = BankingCLI.readLine("Verwendungszweck: ", scanner);
    if (purpose.isEmpty()) {
      purpose = "Auszahlung";
    }

    Transaction withdrawal = new Withdrawal(
        account.getIban(),
        purpose,
        Money.euroOf(amount)
    );

    try {
      account.makeTransaction(withdrawal);
      System.out.println("Auszahlung erfolgreich. Neuer Saldo: " + account.getBalance());
    } catch (InsufficentFundsException e) {
      System.out.println("Fehler: Nicht genügend Guthaben. " + e.getMessage());
    } catch (AccountMismatchException e) {
      System.out.println("Fehler: " + e.getMessage());
    }
  }


  private Account selectAccount() {
    List<Account> accounts = new ArrayList<>(bank.getAccounts());

    if (accounts.isEmpty()) {
      System.out.println("Keine Konten vorhanden. Bitte legen Sie zuerst ein Konto an.");
      return null;
    }

    System.out.println("Konto auswählen:");
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

}
