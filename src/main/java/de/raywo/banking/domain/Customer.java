package de.raywo.banking.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Repräsentiert einen Bankkunden mit automatisch generierter ID, Name und Stadt.
 *
 * <p>Diese Klasse demonstriert mehrere zentrale Java-Konzepte:</p>
 *
 * <ul>
 *   <li><b>Kapselung:</b> Alle Felder sind {@code private}. Der Zugriff erfolgt
 *       ausschließlich über Getter und Setter. Die Setter validieren ihre Parameter
 *       mit {@link Objects#requireNonNull}, um ungültige Zustände zu verhindern.</li>
 *   <li><b>Konstruktor:</b> Der Konstruktor erzwingt, dass Name und Stadt beim
 *       Erstellen angegeben werden, und generiert automatisch eine eindeutige
 *       {@link UUID} als ID.</li>
 *   <li><b>final-Felder:</b> Die {@code id} ist als {@code final} deklariert und
 *       kann nach der Erzeugung nicht mehr verändert werden.</li>
 *   <li><b>equals/hashCode:</b> Beide Methoden basieren auf der {@code id}. Dadurch
 *       gelten zwei {@code Customer}-Objekte mit derselben ID als gleich – unabhängig
 *       davon, ob sich Name oder Stadt geändert haben. Beide Methoden müssen gemeinsam
 *       überschrieben werden, damit {@link java.util.HashMap} und {@link java.util.HashSet}
 *       korrekt funktionieren.</li>
 *   <li><b>toString:</b> Liefert eine lesbare Textdarstellung, die z.&nbsp;B. bei
 *       {@code System.out.println} automatisch verwendet wird.</li>
 *   <li><b>Serializable:</b> Ermöglicht das Speichern und Laden von Kunden-Objekten
 *       über {@link java.io.ObjectOutputStream}/{@link java.io.ObjectInputStream}.</li>
 * </ul>
 *
 * <p><b>Konzepte:</b> Klassen und Objekte, Kapselung, Getter/Setter, Konstruktoren,
 * final-Felder, equals/hashCode/toString, Serialisierung, Generics
 * ({@code Identifiable<UUID>})</p>
 *
 * @see Identifiable
 */
public class Customer implements Serializable, Identifiable<UUID> {

  private final UUID id;
  private String name;
  private String city;


  /**
   * Erzeugt einen neuen Kunden mit automatisch generierter UUID.
   *
   * @param name der Name des Kunden, darf nicht {@code null} sein
   * @param city die Stadt des Kunden, darf nicht {@code null} sein
   * @throws NullPointerException falls {@code name} oder {@code city} {@code null} ist
   */
  public Customer(String name, String city) {
    Objects.requireNonNull(name, "name darf nicht null sein");
    Objects.requireNonNull(city, "city darf nicht null sein");

    this.id = UUID.randomUUID();
    this.name = name;
    this.city = city;
  }


  public String getName() {
    return name;
  }


  /**
   * Setzt den Namen des Kunden. Demonstriert Kapselung: Der Setter prüft,
   * ob der übergebene Wert {@code null} ist, und verhindert so ungültige Zustände.
   *
   * @param name der neue Name, darf nicht {@code null} sein
   * @throws NullPointerException falls {@code name} {@code null} ist
   */
  public void setName(String name) {
    Objects.requireNonNull(name, "name darf nicht null sein");
    this.name = name;
  }


  public String getCity() {
    return city;
  }


  /**
   * @param city die neue Stadt, darf nicht {@code null} sein
   * @throws NullPointerException falls {@code city} {@code null} ist
   */
  public void setCity(String city) {
    Objects.requireNonNull(city, "city darf nicht null sein");
    this.city = city;
  }


  @Override
  public UUID getId() {
    return id;
  }


  /**
   * Vergleicht diesen Kunden mit einem anderen Objekt anhand der {@code id}.
   * Zwei Kunden mit derselben ID gelten als gleich, auch wenn sich andere
   * Felder unterscheiden.
   */
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Customer customer = (Customer) o;
    return id.equals(customer.id);
  }


  /**
   * Gibt den Hash-Code basierend auf der {@code id} zurück. Muss konsistent
   * mit {@link #equals(Object)} sein, damit Kunden in {@link java.util.HashMap}
   * und {@link java.util.HashSet} korrekt funktionieren.
   */
  @Override
  public int hashCode() {
    return id.hashCode();
  }


  @Override
  public String toString() {
    String idChunk = id.toString().substring(0, 8);

    return "[" + idChunk + "]: " +
        name +
        " (" + city + ")";
  }

}
