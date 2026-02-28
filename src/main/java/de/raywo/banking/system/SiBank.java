package de.raywo.banking.system;

import de.raywo.banking.domain.Account;
import de.raywo.banking.domain.Customer;
import de.raywo.banking.persistence.AccountRepository;
import de.raywo.banking.persistence.CustomerRepository;
import de.raywo.banking.persistence.FileStorage;
import de.raywo.banking.persistence.Repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Collection;
import java.util.UUID;

/**
 * Zentrale Fassade des BankingSystems – verwaltet Konten und Kunden und kapselt
 * den Zugriff auf die Repositories.
 *
 * <p>Diese Klasse vereint zwei wichtige Design-Patterns:</p>
 *
 * <h3>Singleton-Pattern</h3>
 * <p>Es existiert genau eine Instanz von {@code SiBank}. Der Konstruktor ist
 * {@code private}, sodass kein Code außerhalb der Klasse ein Objekt erzeugen kann.
 * Erzeugung und Zugriff sind in zwei separate statische Methoden aufgeteilt:</p>
 * <ul>
 *   <li>{@link #initialize(String, String, String)} – wird genau einmal beim
 *       Programmstart aufgerufen und erzeugt die Instanz. Ein doppelter Aufruf
 *       führt zu einer {@link IllegalStateException}.</li>
 *   <li>{@link #getInstance()} – liefert die bereits erzeugte Instanz zurück.
 *       Wird sie vor {@code initialize()} aufgerufen, wird ebenfalls eine
 *       {@link IllegalStateException} geworfen.</li>
 * </ul>
 * <p>Diese Trennung verhindert, dass Konfigurationsparameter bei späteren Aufrufen
 * stillschweigend ignoriert werden, und befreit den Aufrufercode davon, die
 * Konfigurationsdaten jedes Mal mitschleppen zu müssen.</p>
 *
 * <h3>Facade-Pattern</h3>
 * <p>{@code SiBank} bietet der Außenwelt (insbesondere der UI-Schicht) eine
 * vereinfachte Schnittstelle. Die Menü-Klassen greifen ausschließlich über
 * {@code SiBank} auf Konten und Kunden zu und kennen die Repositories nicht
 * direkt. Dadurch bleibt die UI-Schicht unabhängig von der Datenhaltung.</p>
 *
 * <p>Die Felder {@code name}, {@code city} und {@code bic} sind {@code final},
 * da sich die Stammdaten der Bank nach der Initialisierung nicht mehr ändern.</p>
 *
 * <p><b>Hinweis:</b> Diese Singleton-Implementierung ist nicht thread-sicher.
 * Für Anwendungen mit mehreren Threads müsste die Erzeugung zusätzlich
 * synchronisiert werden.</p>
 *
 * <p><b>Konzepte:</b> Singleton-Pattern, Facade-Pattern, Schichtenarchitektur,
 * final-Felder, Optional ({@link #getAccount(String)})</p>
 *
 * @see de.raywo.banking.persistence.Repository
 * @see de.raywo.banking.ui.BankingCLI
 */
public class SiBank {

  private final String name;
  private final String city;
  private final String bic;
  private final Repository<String, Account> accountRepository;
  private final Repository<UUID, Customer> customerRepository;

  private static SiBank instance;


  /**
   * Erzeugt und initialisiert die einzige {@code SiBank}-Instanz.
   * Darf nur einmal aufgerufen werden.
   *
   * @param name der Name der Bank
   * @param city der Sitz der Bank
   * @param bic  der BIC (Bank Identifier Code)
   * @throws IllegalStateException falls bereits eine Instanz existiert
   */
  public static void initialize(String name, String city, String bic) {
    if (instance != null) {
      throw new IllegalStateException("SiBank wurde bereits initialisiert.");
    }
    instance = new SiBank(name, city, bic);
  }


  /**
   * Gibt die einzige {@code SiBank}-Instanz zurück.
   *
   * @return die Singleton-Instanz
   * @throws IllegalStateException falls {@link #initialize} noch nicht aufgerufen wurde
   */
  public static SiBank getInstance() {
    if (instance == null) {
      throw new IllegalStateException("SiBank wurde noch nicht initialisiert. Bitte zuerst initialize() aufrufen.");
    }
    return instance;
  }


  /**
   * Privater Konstruktor – kann nur von {@link #initialize} aufgerufen werden.
   * Erzeugt die Repositories und lädt vorhandene Daten aus den Binärdateien.
   */
  private SiBank(String name, String city, String bic) {
    this.name = name;
    this.city = city;
    this.bic = bic;
    this.accountRepository = new AccountRepository(new FileStorage<>("accounts.bin"));
    this.customerRepository = new CustomerRepository(new FileStorage<>("customers.bin"));

    try {
      this.accountRepository.initialize();
      this.customerRepository.initialize();
    } catch (FileNotFoundException | NoSuchFileException e) {
      System.out.println("Keine Datendateien gefunden. Starte mit leeren Daten.");
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("Fehler beim Laden der Datendateien.", e);
    }
  }


  public String getName() {
    return name;
  }


  public String getCity() {
    return city;
  }


  public String getBic() {
    return bic;
  }


  /** @return alle gespeicherten Konten */
  public Collection<Account> getAccounts() {
    return accountRepository.findAll();
  }


  /** @return alle gespeicherten Kunden */
  public Collection<Customer> getCustomers() {
    return customerRepository.findAll();
  }


  /** Speichert ein Konto im Repository. */
  public void addAccount(Account account) {
    accountRepository.save(account);
  }


  /** Löscht ein Konto anhand seiner IBAN. */
  public void removeAccount(String iban) {
    accountRepository.deleteById(iban);
  }


  /**
   * Sucht ein Konto anhand seiner IBAN.
   *
   * <p>Demonstriert den Umgang mit {@link java.util.Optional}: Die Methode
   * {@code findById} des Repositories gibt ein {@code Optional} zurück.
   * Mit {@code orElseThrow} wird entweder das gefundene Konto zurückgegeben
   * oder eine {@link NotFoundException} geworfen.</p>
   *
   * @param iban die IBAN des gesuchten Kontos
   * @return das gefundene Konto
   * @throws NotFoundException falls kein Konto mit dieser IBAN existiert
   */
  public Account getAccount(String iban) throws NotFoundException {
    return accountRepository
        .findById(iban)
        .orElseThrow(() -> new NotFoundException("Ein Konto mit der IBAN " + iban + " existiert nicht"));
  }


  /** Speichert einen Kunden im Repository. */
  public void addCustomer(Customer customer) {
    customerRepository.save(customer);
  }


  /** Löscht einen Kunden aus dem Repository. */
  public void removeCustomer(Customer customer) {
    customerRepository.delete(customer);
  }


  /**
   * Persistiert alle Konten und Kunden in die Binärdateien.
   *
   * @throws IOException falls beim Schreiben ein Fehler auftritt
   */
  public void persist() throws IOException {
    accountRepository.persist();
    customerRepository.persist();
  }


  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    SiBank siBank = (SiBank) o;
    return bic.equals(siBank.bic);
  }


  @Override
  public int hashCode() {
    return bic.hashCode();
  }


  @Override
  public String toString() {
    return name + " (" + city + ")" +
        ", BIC: " + bic;
  }

}
