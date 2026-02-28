package de.raywo.banking;

import de.raywo.banking.system.SiBank;
import de.raywo.banking.ui.BankingCLI;

/**
 * Einstiegspunkt für die interaktive CLI-Benutzeroberfläche des BankingSystems.
 *
 * <p>Diese Klasse initialisiert die {@link SiBank} als Singleton, lädt bzw.
 * erzeugt Beispieldaten über {@link DataInitializer} und startet die
 * CLI-Menüführung über {@link BankingCLI}.</p>
 *
 * <p>Die Trennung von {@link Main} (nicht-interaktive Konsolenausgabe) und
 * {@code MainUI} (interaktive CLI) zeigt, wie durch die
 * <b>Schichtenarchitektur</b> unterschiedliche Benutzeroberflächen auf
 * dieselbe Geschäftslogik ({@link SiBank}) zugreifen können, ohne diese
 * anpassen zu müssen.</p>
 *
 * @see Main
 * @see BankingCLI
 * @see SiBank#initialize(String, String, String)
 */
public class MainUI {

  /**
   * Startet die Anwendung: Initialisiert das Singleton, befüllt bei Bedarf
   * Beispieldaten und startet die interaktive CLI.
   *
   * @param args Kommandozeilenargumente (werden nicht verwendet)
   */
  public static void main(String[] args) {
    SiBank.initialize("Signal Iduna Bank", "Hamburg", "SIBAHH26");
    SiBank bank = SiBank.getInstance();

    if (bank.getAccounts().isEmpty()) {
      DataInitializer.initializeData(bank);
    }

    new BankingCLI(bank).start();
  }

}
