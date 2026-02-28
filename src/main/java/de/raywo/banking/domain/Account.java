package de.raywo.banking.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Abstrakte Basisklasse für alle Kontoarten im BankingSystem.
 *
 * <p>{@code Account} ist {@code abstract} und kann daher nicht direkt instanziiert
 * werden. Sie definiert das gemeinsame Verhalten aller Kontoarten (Saldo, Inhaber,
 * Transaktionen) und wird von {@link CurrentAccount} und {@link SavingsAccount}
 * spezialisiert. Das Schlüsselwort {@code extends} drückt eine „ist-ein"-Beziehung
 * aus: Ein Girokonto <em>ist ein</em> Konto.</p>
 *
 * <p><b>Polymorphie:</b> Die Methode {@link #isAmountAvailable(Money)} kann von
 * Unterklassen überschrieben werden (siehe {@link CurrentAccount}). Wenn
 * {@link #withdraw(Money)} diese Methode aufruft, wird je nach tatsächlichem
 * Kontotyp zur <em>Laufzeit</em> die richtige Implementierung ausgeführt – ohne
 * dass die aufrufende Stelle den konkreten Typ kennen muss.</p>
 *
 * <p>Ebenso nutzt {@link #makeTransaction(Transaction)} Polymorphie: Der Aufruf
 * {@code transaction.applyTo(this)} überlässt es der konkreten Transaktion
 * ({@link Deposit} oder {@link Withdrawal}), ob ein- oder ausgezahlt wird.</p>
 *
 * <p>{@link #getTransactions()} gibt die Transaktionsliste als
 * {@link Collections#unmodifiableList(List) unveränderbare Sicht} zurück, damit
 * Aufrufer die interne Liste nicht manipulieren können.</p>
 *
 * <p><b>Konzepte:</b> Abstrakte Klassen, Vererbung, Polymorphie, Kapselung,
 * final-Felder, equals/hashCode/toString, Collections, Serialisierung</p>
 *
 * @see CurrentAccount
 * @see SavingsAccount
 * @see Transaction
 */
public abstract class Account implements Serializable, Identifiable<String> {

  private final String iban;
  private Money balance;
  private Customer owner;
  private AccountStatus status;
  private final List<Transaction> transactions;


  /**
   * Erzeugt ein neues Konto mit dem angegebenen IBAN und Inhaber.
   * Der Saldo wird auf 0,00 EUR initialisiert, der Status auf {@link AccountStatus#ACTIVE}.
   *
   * <p>Unterklassen rufen diesen Konstruktor mit {@code super(iban, owner)} auf,
   * um die gemeinsamen Felder zu initialisieren.</p>
   *
   * @param iban  die IBAN des Kontos (unveränderlich nach Erzeugung)
   * @param owner der Kontoinhaber
   * @throws NullPointerException falls {@code iban} oder {@code owner} {@code null} ist
   */
  public Account(String iban, Customer owner) {
    Objects.requireNonNull(iban, "iban darf nicht null sein");
    Objects.requireNonNull(owner, "owner darf nicht null sein");

    this.iban = iban;
    this.owner = owner;
    this.balance = Money.zeroEuro();
    this.status = AccountStatus.ACTIVE;
    this.transactions = new ArrayList<>();
  }


  /** Gibt die IBAN als eindeutigen Identifier zurück (verwendet von {@link Identifiable}). */
  @Override
  public String getId() {
    return iban;
  }


  public String getIban() {
    return iban;
  }


  public Money getBalance() {
    return balance;
  }


  public Customer getOwner() {
    return owner;
  }


  public void setOwner(Customer owner) {
    this.owner = owner;
  }


  public AccountStatus getStatus() {
    return status;
  }


  public void setStatus(AccountStatus status) {
    this.status = status;
  }


  /**
   * Gibt eine <b>unveränderbare Sicht</b> auf die Transaktionsliste zurück.
   *
   * <p>{@link Collections#unmodifiableList(List)} stellt sicher, dass Aufrufer
   * die interne Liste nicht verändern können (z.&nbsp;B. durch {@code add} oder
   * {@code remove}). Die zurückgegebene Liste spiegelt aber weiterhin
   * Änderungen der internen Liste wider.</p>
   *
   * @return eine unveränderbare Sicht auf die Transaktionen dieses Kontos
   */
  public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(transactions);
  }


  /**
   * Führt eine Transaktion auf diesem Konto durch.
   *
   * <p>Hier zeigt sich <b>Polymorphie</b>: Der Aufruf {@code transaction.applyTo(this)}
   * delegiert an die konkrete Transaktion ({@link Deposit} oder {@link Withdrawal}).
   * Diese Methode muss den konkreten Typ nicht kennen.</p>
   *
   * @param transaction die durchzuführende Transaktion
   * @throws InactiveAccountException   falls das Konto inaktiv ist
   * @throws AccountMismatchException   falls die IBAN der Transaktion nicht zum Konto passt
   * @throws InsufficientFundsException falls bei einer Abhebung das Guthaben nicht ausreicht
   */
  public void makeTransaction(Transaction transaction) throws InsufficientFundsException, AccountMismatchException, InactiveAccountException {
    if (status != AccountStatus.ACTIVE) {
      throw new InactiveAccountException("Das Konto " + iban + " ist inaktiv und lässt keine Transaktionen zu.");
    }

    if (!transaction.getIban().equals(iban)) {
      throw new AccountMismatchException("Die IBAN der Transaktion passt nicht zur IBAN des Kontos.");
    }

    transaction.applyTo(this);
    this.transactions.add(transaction);
  }


  /**
   * Erhöht den Kontostand um den angegebenen Betrag. Paket-privat, damit nur
   * Transaktionen innerhalb des {@code domain}-Pakets diese Methode aufrufen können.
   *
   * @param amount der einzuzahlende Betrag
   */
  void deposit(Money amount) {
    this.balance = this.balance.add(amount);
  }


  /**
   * Reduziert den Kontostand um den angegebenen Betrag. Prüft vorher über
   * {@link #isAmountAvailable(Money)}, ob genügend Guthaben vorhanden ist.
   *
   * <p>Paket-privat, damit nur Transaktionen innerhalb des {@code domain}-Pakets
   * diese Methode aufrufen können.</p>
   *
   * @param amount der abzuhebende Betrag
   * @throws InsufficientFundsException falls das verfügbare Guthaben nicht ausreicht
   */
  void withdraw(Money amount) throws InsufficientFundsException {
    if (!isAmountAvailable(amount)) {
      throw new InsufficientFundsException("Der abzuhebende Betrag übersteigt das verfügbare Guthaben.");
    }

    this.balance = balance.subtract(amount);
  }


  /** Gleichheit basiert auf der IBAN – zwei Konten mit derselben IBAN gelten als gleich. */
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Account account = (Account) o;
    return iban.equals(account.iban);
  }


  @Override
  public int hashCode() {
    return iban.hashCode();
  }


  @Override
  public String toString() {
    return "[" + iban + "]" +
        ", Saldo: " + balance +
        ", Inhaber: " + owner +
        ", (" + status + ")";
  }


  /**
   * Prüft, ob der angegebene Betrag verfügbar ist. In der Basisimplementierung
   * wird nur der aktuelle Saldo berücksichtigt.
   *
   * <p><b>Polymorphie:</b> {@link CurrentAccount} überschreibt diese Methode, um
   * zusätzlich den Dispositionskredit einzubeziehen. Wenn {@link #withdraw(Money)}
   * diese Methode aufruft, wird je nach Kontotyp zur Laufzeit die richtige
   * Implementierung ausgeführt.</p>
   *
   * @param amount der zu prüfende Betrag
   * @return {@code true}, wenn der Betrag verfügbar ist
   */
  protected boolean isAmountAvailable(Money amount) {
    Comparator<Money> comparator = Money.sameCurrencyComparator(balance.currency());

    return comparator.compare(amount, balance) <= 0;
  }

}
