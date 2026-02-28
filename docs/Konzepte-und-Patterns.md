# Java-Konzepte und Design-Patterns im BankingSystem

Diese Dokumentation fasst die im Training behandelten Java-Sprachfeatures,
objektorientierten Konzepte und Design-Patterns zusammen. Sie folgt der
Reihenfolge, in der die Themen im Training erarbeitet wurden, und dient als
Nachschlagewerk zum Auffrischen.

## Inhaltsverzeichnis

1. [Klassen und Objekte](#1-klassen-und-objekte)
2. [Kapselung, Getter/Setter und Konstruktoren](#2-kapselung-gettersetter-und-konstruktoren)
3. [equals, hashCode und toString](#3-equals-hashcode-und-tostring)
4. [Exceptions (Checked vs. Unchecked)](#4-exceptions-checked-vs-unchecked)
5. [Vererbung und abstrakte Klassen](#5-vererbung-und-abstrakte-klassen)
6. [Polymorphie und Sealed Classes](#6-polymorphie-und-sealed-classes)
7. [Records und Value Objects](#7-records-und-value-objects)
8. [Interfaces und Generics](#8-interfaces-und-generics)
9. [Collections und Optional](#9-collections-und-optional)
10. [Serialisierung und Try-with-Resources](#10-serialisierung-und-try-with-resources)
11. [Abstrakte Klassen als Basisimplementierung](#11-abstrakte-klassen-als-basisimplementierung)
12. [Singleton-Pattern](#12-singleton-pattern)
13. [Schichtenarchitektur](#13-schichtenarchitektur)

---

## 1. Klassen und Objekte

Eine Klasse ist der Bauplan für Objekte. Sie definiert, welche Daten (Felder)
ein Objekt besitzt und welche Operationen (Methoden) es anbietet. Ein Objekt ist
eine konkrete Instanz einer Klasse, die mit `new` erzeugt wird.

Im Projekt sind `Customer` und `Account` die zentralen Klassen. Ein `Customer`
besitzt eine automatisch generierte ID, einen Namen und eine Stadt. Ein
`Account` hat eine IBAN, einen Kontostand, einen Inhaber, einen Status
(`AccountStatus`) und eine Liste von Transaktionen.

**Siehe:** `domain/Customer.java`, `domain/Account.java`

## 2. Kapselung, Getter/Setter und Konstruktoren

Kapselung bedeutet, dass die Felder einer Klasse als `private` deklariert werden
und der Zugriff ausschließlich über öffentliche Methoden (Getter und Setter)
erfolgt. Dadurch kann die Klasse kontrollieren, welche Werte gesetzt werden
dürfen.

In der Klasse `Customer` sind alle Felder `private`. Der Setter `setName` prüft
beispielsweise, ob der übergebene Wert `null` ist, und verhindert so ungültige
Zustände:

```java
public void setName(String name) {
  Objects.requireNonNull(name, "name darf nicht null sein");
  this.name = name;
}
```

Konstruktoren werden verwendet, um Objekte in einem gültigen Anfangszustand zu
erzeugen. Der Konstruktor von `Customer` erzwingt, dass Name und Stadt beim
Erstellen angegeben werden, und generiert automatisch eine eindeutige ID.

Felder, die sich nach der Erzeugung nicht mehr ändern sollen, werden als `final`
deklariert. So ist beispielsweise die `id` eines `Customer` und die `iban` eines
`Account` nach dem Erstellen unveränderlich.

**Siehe:** `domain/Customer.java`, `domain/Account.java`

## 3. equals, hashCode und toString

Standardmäßig vergleicht Java Objekte über ihre Referenz (Speicheradresse). Wenn
zwei Objekte mit denselben Daten als „gleich“ gelten sollen, müssen Sie die
Methoden `equals` und `hashCode` überschreiben.

In `Customer` basiert die Gleichheit auf der `id`, in `Account` auf der `iban`.
Beide Methoden werden gemeinsam überschrieben, da Java verlangt, dass Objekte,
die laut `equals` gleich sind, auch denselben `hashCode` liefern. Dies ist
besonders für die korrekte Funktionsweise von `HashMap` und `HashSet` wichtig.

Die Methode `toString` liefert eine lesbare Textdarstellung eines Objekts. Sie
wird automatisch aufgerufen, wenn ein Objekt z. B. mit `System.out.println`
ausgegeben wird. Im Projekt überschreiben alle wesentlichen Klassen diese
Methode, um aussagekräftige Ausgaben zu erzeugen.

**Siehe:** `domain/Customer.java` (Zeilen 52–64), `domain/Account.java` (Zeilen
96–117)

## 4. Exceptions (Checked vs. Unchecked)

Java unterscheidet zwei Arten von Exceptions:

**Checked Exceptions** erben von `Exception` und müssen vom Aufrufer entweder
mit `try-catch` behandelt oder mit `throws` weitergereicht werden. Sie
repräsentieren erwartbare Fehlersituationen, auf die das Programm reagieren
sollte. Im Projekt sind das:

- `InsufficientFundsException` – zu wenig Guthaben für eine Abhebung
- `AccountMismatchException` – die IBAN einer Transaktion passt nicht zum Konto
- `InactiveAccountException` – eine Transaktion wird auf einem inaktiven Konto
  versucht
- `NotFoundException` – ein gesuchtes Objekt wurde nicht gefunden

**Unchecked Exceptions** erben von `RuntimeException` und müssen nicht explizit
behandelt werden. Sie deuten auf Programmierfehler hin, die im Normalfall nicht
auftreten sollten:

- `CurrencyMismatchException` – eine Rechenoperation mit unterschiedlichen
  Währungen

Der Compiler erzwingt bei Checked Exceptions, dass Sie sich um den Fehlerfall
kümmern. Bei Unchecked Exceptions tut er das nicht, weil davon ausgegangen wird,
dass der Programmierer den Fehler durch korrekten Code vermeidet.

Die Entscheidung, ob eine Exception checked oder unchecked sein soll, hängt
davon ab, ob es sich um eine erwartbare Geschäftssituation handelt (checked)
oder um einen Programmierfehler, der durch korrekten Code vermeidbar ist
(unchecked). Ein inaktives Konto ist z. B. eine reguläre Geschäftssituation –
daher ist `InactiveAccountException` eine Checked Exception. Ein
Währungs-Mismatch hingegen sollte im normalen Betrieb nie auftreten und ist
daher unchecked.

**Siehe:** `domain/InsufficientFundsException.java`,
`domain/InactiveAccountException.java`,
`domain/CurrencyMismatchException.java`, `system/NotFoundException.java`

## 5. Vererbung und abstrakte Klassen

Vererbung ermöglicht es, gemeinsames Verhalten in einer Basisklasse zu
definieren und in Unterklassen zu spezialisieren. Das Schlüsselwort `extends`
drückt eine „ist-ein“-Beziehung aus.

Im Projekt ist `Account` eine abstrakte Klasse. Sie kann nicht direkt
instanziiert werden, definiert aber das gemeinsame Verhalten aller Kontoarten 
(Saldo, Inhaber, Transaktionen). Die konkreten Klassen `CurrentAccount` und
`SavingsAccount` erben diese Grundfunktionalität und ergänzen sie um eigene
Eigenschaften:

- `CurrentAccount` fügt einen Dispositionskredit (`limit`) und einen
  Sollzinssatz hinzu
- `SavingsAccount` fügt einen Habenzinssatz hinzu

Der Konstruktor der Unterklasse ruft mit `super(...)` den Konstruktor der
Basisklasse auf, um die gemeinsamen Felder zu initialisieren:

```java
public CurrentAccount(String iban, Customer owner) {
  super(iban, owner);
  this.debitInterestRate = 0.00f;
  this.limit = Money.zeroEuro();
}
```

**Siehe:** `domain/Account.java`, `domain/CurrentAccount.java`,
`domain/SavingsAccount.java`

## 6. Polymorphie und Sealed Classes

### Polymorphie

Polymorphie bedeutet, dass eine Variable vom Typ der Basisklasse auf ein Objekt
einer Unterklasse verweisen kann. Welche Implementierung einer Methode
aufgerufen wird, entscheidet sich erst zur Laufzeit.

Im Projekt überschreibt `CurrentAccount` die Methode `isAmountAvailable`, um den
Dispositionskredit zu berücksichtigen. Wenn `Account.withdraw()` diese Methode
aufruft, wird je nach tatsächlichem Kontotyp die richtige Implementierung
ausgeführt – ohne dass die aufrufende Stelle den konkreten Typ kennen muss.

Ebenso überschreiben `Deposit` und `Withdrawal` die abstrakte Methode `applyTo`
der Klasse `Transaction`. Die Methode `Account.makeTransaction()` prüft zunächst,
ob das Konto aktiv ist (`AccountStatus.ACTIVE`), und ruft dann
`transaction.applyTo(this)` auf. Die konkrete Transaktion entscheidet, ob
eingezahlt oder abgehoben wird.

Die Status-Prüfung in `makeTransaction()` ist ein Beispiel dafür, dass fachliche
Regeln im Domain-Modell durchgesetzt werden – nicht in der UI oder der
System-Schicht. Dadurch ist die Regel unabhängig vom Aufrufer gewährleistet.

### Sealed Classes

Sealed Classes (seit Java 17) schränken ein, welche Klassen von einer
Basisklasse erben dürfen. Dies geschieht mit den Schlüsselwörtern `sealed` und
`permits`:

```java
sealed public abstract class Transaction implements Serializable
    permits Deposit, Withdrawal {
```

Damit ist garantiert, dass es nur `Deposit` und `Withdrawal` als
Transaktionstypen gibt. Die erlaubten Unterklassen müssen als `final`, `sealed`
oder `non-sealed` deklariert werden – im Projekt sind `Deposit` und `Withdrawal`
jeweils `final`.

### Pattern Matching bei instanceof

Seit Java 16 können Sie beim `instanceof`-Check direkt eine typisierte Variable
einführen, ohne einen separaten Cast zu benötigen:

```java
if(account instanceof CurrentAccount currentAccount) {
  currentAccount.setLimit(Money.euroOf(limitValue));
}
```

**Siehe:** `domain/Transaction.java`, `domain/Deposit.java`,
`domain/Withdrawal.java`, `ui/AccountMenu.java` (Zeile 122)

## 7. Records und Value Objects

Ein Record (seit Java 14) ist eine kompakte Möglichkeit, unveränderliche
Datenklassen zu definieren. Der Compiler generiert automatisch den Konstruktor,
Getter, `equals`, `hashCode` und `toString`.

Die Klasse `Money` ist als Record implementiert und repräsentiert einen
Geldbetrag mit Währung. Ein sogenannter *Compact Constructor* ermöglicht
Validierung und Normalisierung direkt bei der Erzeugung:

```java
public record Money(BigDecimal amount,
                    Currency currency) implements Serializable {
  public Money {
    Objects.requireNonNull(amount);
    Objects.requireNonNull(currency);
    amount = amount.setScale(2, RoundingMode.HALF_UP);
  }
}
```

Records eignen sich besonders für Value Objects – Objekte, die durch ihre Werte
definiert sind (nicht durch eine Identität). Zwei `Money`-Objekte mit gleichem
Betrag und gleicher Währung sind immer gleich, unabhängig davon, wann oder wo
sie erzeugt wurden.

Die statischen Methoden `euroOf()` und `zeroEuro()` sind sogenannte *Factory
Methods*, die das Erzeugen von `Money`-Objekten vereinfachen und die Absicht des
Codes lesbarer machen.

**Siehe:** `domain/Money.java`

## 8. Interfaces und Generics

### Interfaces

Ein Interface definiert einen Vertrag: Es legt fest, welche Methoden eine Klasse
anbieten muss, ohne deren Implementierung vorzugeben. Klassen implementieren ein
Interface mit dem Schlüsselwort `implements`.

Im Projekt gibt es drei zentrale Interfaces:

- `Identifiable<T>` – garantiert, dass eine Klasse eine `getId()`-Methode
  besitzt
- `Repository<Id, T>` – definiert die CRUD-Operationen für die Datenhaltung
- `Storage<Id, T>` – abstrahiert den Speichermechanismus (z. B. Datei, Datenbank)

### Generics

Generics ermöglichen es, Klassen und Interfaces mit Typparametern zu versehen,
sodass sie typsicher mit verschiedenen Datentypen arbeiten können. Statt für
Kunden und Konten jeweils ein eigenes Repository zu schreiben, gibt es ein
generisches `Repository<Id, T>`:

```java
public interface Repository<Id, T extends Identifiable<Id>> {
  void save(T entity);

  Optional<T> findById(Id id);

  Collection<T> findAll();
  // ...
}
```

Die Typeinschränkung `T extends Identifiable<Id>` stellt sicher, dass nur
Objekte gespeichert werden können, die eine `getId()`-Methode besitzen. Beim
konkreten Einsatz werden die Typparameter festgelegt:

- `AccountRepository` arbeitet mit `<String, Account>` (IBAN als Schlüssel)
- `CustomerRepository` arbeitet mit `<UUID, Customer>` (UUID als Schlüssel)

**Siehe:** `domain/Identifiable.java`, `persistence/Repository.java`,
`persistence/Storage.java`, `persistence/AccountRepository.java`,
`persistence/CustomerRepository.java`

## 9. Collections und Optional

### Collections

Das Java Collections Framework stellt vorgefertigte Datenstrukturen bereit. Im
Projekt werden verwendet:

- `HashMap<Id, T>` – speichert Schlüssel-Wert-Paare und wird in
  `AbstractMapBasedRepository` als In-Memory-Speicher genutzt
- `ArrayList<Transaction>` – speichert die Liste der Transaktionen eines Kontos
- `Collections.unmodifiableList()` – erzeugt eine nicht veränderbare Sicht auf
  eine Liste, sodass Aufrufer die interne Transaktionsliste nicht manipulieren
  können

Methoden geben bevorzugt die allgemeinen Interface-Typen `Collection<T>` oder
`List<T>` zurück, nicht die konkreten Implementierungen. Das macht den Code
flexibler gegenüber späteren Änderungen.

### Optional

`Optional<T>` repräsentiert einen Wert, der vorhanden sein kann oder nicht. Es
ist eine Alternative zu `null`-Rückgaben und macht im Code explizit sichtbar,
dass ein Ergebnis fehlen könnte.

Die Methode `findById` im Repository gibt ein `Optional` zurück. In
`SiBank.getAccount()` wird mit `orElseThrow` entweder das gefundene Konto
zurückgegeben oder eine Exception geworfen:

```java
public Account getAccount(String iban) throws NotFoundException {
  return accountRepository
      .findById(iban)
      .orElseThrow(() -> new NotFoundException("Ein Konto mit der IBAN " + iban + " existiert nicht"));
}
```

**Siehe:** `persistence/AbstractMapBasedRepository.java`,
`domain/Account.java` (Zeile 68), `system/SiBank.java` (Zeilen 95–99)

## 10. Serialisierung und Try-with-Resources

### Serialisierung

Serialisierung wandelt ein Objekt in einen Bytestrom um, der in eine Datei
geschrieben werden kann. Deserialisierung ist der umgekehrte Vorgang. Damit eine
Klasse serialisierbar ist, muss sie das Marker-Interface `Serializable`
implementieren – das gilt auch für alle Felder, die mitgespeichert werden
sollen.

Im Projekt implementieren `Customer`, `Account`, `Transaction` und `Money`
dieses Interface. Die Klasse `FileStorage` nutzt `ObjectOutputStream` und
`ObjectInputStream`, um die gesamte Map aller Objekte in eine Binärdatei zu
schreiben bzw. daraus zu lesen.

### Try-with-Resources

Ressourcen wie Dateiströme müssen nach der Verwendung geschlossen werden. Die
`try-with-resources`-Syntax stellt sicher, dass dies auch im Fehlerfall
automatisch geschieht:

```java
try(var out = Files.newOutputStream(file, ...);
    var oos = new ObjectOutputStream(out)){
  oos.writeObject(collection);
}
```

Die in der Klammer deklarierten Ressourcen werden am Ende des `try`-Blocks
automatisch geschlossen – auch wenn eine Exception auftritt. Die Ressourcen
müssen dafür das Interface `AutoCloseable` implementieren.

**Siehe:** `persistence/FileStorage.java`

## 11. Abstrakte Klassen als Basisimplementierung

Abstrakte Klassen können neben abstrakten Methoden auch fertige
Implementierungen enthalten. Dies ist besonders nützlich, wenn mehrere
Unterklassen dasselbe Grundverhalten teilen.

Die Klasse `AbstractMapBasedRepository` implementiert das `Repository`-Interface
vollständig auf Basis einer `HashMap`. Die konkreten Repositories müssen
lediglich die Typparameter festlegen:

```java
public class AccountRepository extends AbstractMapBasedRepository<String, Account> {
  public AccountRepository(Storage<String, Account> storage) {
    super(storage);
  }
}
```

Dieses Muster vermeidet Code-Duplizierung: Statt in `AccountRepository` und
`CustomerRepository` jeweils `save`, `findById`, `findAll` usw. identisch zu
implementieren, erben beide die Implementierung von
`AbstractMapBasedRepository`. Sollte sich die Speicherlogik ändern, muss nur
eine Stelle angepasst werden.

Die eigentliche Persistierung wird an das `Storage`-Interface delegiert. Dadurch
bleibt das Repository unabhängig davon, ob die Daten in eine Datei, eine
Datenbank oder einen anderen Speicher geschrieben werden (siehe
auch [Strategy-Pattern](https://en.wikipedia.org/wiki/Strategy_pattern)).

**Siehe:** `persistence/AbstractMapBasedRepository.java`,
`persistence/AccountRepository.java`, `persistence/CustomerRepository.java`

## 12. Singleton-Pattern

Das [Singleton-Pattern](https://en.wikipedia.org/wiki/Singleton_pattern) stellt
sicher, dass von einer Klasse nur genau eine Instanz existiert und bietet einen
globalen Zugriffspunkt auf diese Instanz.

In `SiBank` ist der Konstruktor `private`, sodass kein Code außerhalb der Klasse
ein Objekt erzeugen kann. Die Erzeugung und der Zugriff sind in zwei separate
statische Methoden aufgeteilt:

```java
private static SiBank instance;


public static void initialize(String name, String city, String bic) {
  if (instance != null) {
    throw new IllegalStateException("SiBank wurde bereits initialisiert.");
  }
  instance = new SiBank(name, city, bic);
}


public static SiBank getInstance() {
  if (instance == null) {
    throw new IllegalStateException(
        "SiBank wurde noch nicht initialisiert. Bitte zuerst initialize() aufrufen.");
  }
  return instance;
}


private SiBank(String name, String city, String bic) { ... }
```

`initialize()` wird genau einmal beim Programmstart aufgerufen und erzeugt die
Instanz mit den Konfigurationsdaten. Ein doppelter Aufruf führt zu einer
`IllegalStateException`. Danach liefert `getInstance()` ohne Parameter die
bereits erzeugte Instanz zurück. Wird `getInstance()` vor `initialize()`
aufgerufen, wird ebenfalls eine `IllegalStateException` geworfen.

Diese Trennung hat zwei Vorteile gegenüber einer einzigen
`getInstance(String, String, String)`-Methode: Erstens wird verhindert, dass die
Konfigurationsparameter bei späteren Aufrufen stillschweigend ignoriert werden.
Zweitens muss der Aufrufercode die Konfigurationsdaten nicht jedes Mal
mitschleppen, wenn er nur die Instanz benötigt.

Die Felder `name` und `city` sind als `final` deklariert, da sich die
Stammdaten der Bank nach der Initialisierung nicht mehr ändern.

> **Hinweis:** Diese Implementierung ist nicht thread-sicher. Für Anwendungen
> mit mehreren Threads müsste die Erzeugung zusätzlich synchronisiert werden.

**Siehe:** `system/SiBank.java`

## 13. Schichtenarchitektur

Das Projekt ist in vier Pakete gegliedert, die jeweils eine klar abgegrenzte
Verantwortung haben:

```
ui              Benutzeroberfläche (CLI-Menüs)
  ↓
system          Geschäftslogik und Orchestrierung (SiBank)
  ↓
domain          Fachliche Klassen (Account, Customer, Transaction, Money)
  ↓
persistence     Datenhaltung (Repository, Storage, FileStorage)
```

**`domain`** enthält die fachlichen Klassen, die das Kernmodell der Anwendung
bilden. Sie sind unabhängig von der Benutzeroberfläche und der Art der
Datenspeicherung.

**`persistence`** ist für das Lesen und Schreiben von Daten zuständig. Die
Interfaces `Repository` und `Storage` definieren die Verträge, die konkreten
Klassen (`AccountRepository`, `FileStorage`) liefern die Implementierung.

**`system`** enthält mit `SiBank` die zentrale Fassade 
(siehe [Facade-Pattern](https://en.wikipedia.org/wiki/Facade_pattern)), die 
die Repositories kapselt und der Außenwelt eine vereinfachte Schnittstelle bietet. 

**`ui`** enthält die Benutzeroberfläche. Die Menü-Klassen greifen ausschließlich
über `SiBank` auf die Geschäftslogik zu und kennen die Repositories nicht
direkt.

Diese Trennung hat mehrere Vorteile: Die einzelnen Schichten können unabhängig
voneinander verändert werden. Die Benutzeroberfläche könnte beispielsweise durch
eine grafische Oberfläche ersetzt werden, ohne die fachlichen Klassen oder die
Datenhaltung anpassen zu müssen. Ebenso könnte `FileStorage` durch eine
Datenbankanbindung ausgetauscht werden, ohne den Rest der Anwendung zu berühren.

**Siehe:** Paketstruktur unter `src/main/java/de/raywo/banking/`
