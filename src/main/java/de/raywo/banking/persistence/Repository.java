package de.raywo.banking.persistence;

import de.raywo.banking.domain.Identifiable;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 * Generisches Interface für die Datenhaltung (CRUD-Operationen).
 *
 * <p>Definiert den Vertrag, den alle Repositories erfüllen müssen, ohne die
 * Implementierung vorzugeben. Durch die Typparameter {@code Id} und {@code T}
 * kann dasselbe Interface typsicher für verschiedene Entitäten verwendet werden,
 * statt für Kunden und Konten jeweils ein eigenes Repository zu schreiben.</p>
 *
 * <p>Die Typeinschränkung {@code T extends Identifiable<Id>} stellt sicher, dass
 * nur Objekte gespeichert werden können, die eine {@code getId()}-Methode besitzen.
 * Beim konkreten Einsatz werden die Typparameter festgelegt:</p>
 * <ul>
 *   <li>{@link AccountRepository} arbeitet mit {@code <String, Account>} (IBAN als Schlüssel)</li>
 *   <li>{@link CustomerRepository} arbeitet mit {@code <UUID, Customer>} (UUID als Schlüssel)</li>
 * </ul>
 *
 * <p>Methoden wie {@link #findById(Object)} geben ein {@link Optional} zurück, um
 * explizit sichtbar zu machen, dass ein Ergebnis fehlen kann – anstatt {@code null}
 * zurückzugeben. {@link #findAll()} gibt den allgemeinen Interface-Typ
 * {@link Collection} zurück (nicht die konkrete Implementierung), was den Code
 * flexibler macht.</p>
 *
 * <p><b>Konzepte:</b> Interfaces, Generics (mit Typeinschränkung), Optional,
 * Collection-Rückgabetypen</p>
 *
 * @param <Id> der Typ des Identifiers (z.&nbsp;B. {@link String} oder {@link java.util.UUID})
 * @param <T>  der Typ der gespeicherten Entität (muss {@link Identifiable} implementieren)
 * @see AbstractMapBasedRepository
 * @see Identifiable
 */
public interface Repository<Id, T extends Identifiable<Id>> {

  /**
   * Speichert eine Entität im Repository. Falls bereits eine Entität mit
   * derselben ID existiert, wird sie überschrieben.
   *
   * @param entity die zu speichernde Entität
   */
  void save(T entity);

  /**
   * Sucht eine Entität anhand ihrer ID.
   *
   * @param id die ID der gesuchten Entität
   * @return ein {@link Optional} mit der Entität, oder {@link Optional#empty()}
   *         falls nicht gefunden
   */
  Optional<T> findById(Id id);

  /**
   * Gibt alle gespeicherten Entitäten zurück.
   *
   * @return eine {@link Collection} aller Entitäten (kann leer sein)
   */
  Collection<T> findAll();

  /**
   * Löscht die angegebene Entität aus dem Repository.
   *
   * @param entity die zu löschende Entität
   */
  void delete(T entity);

  /**
   * Löscht eine Entität anhand ihrer ID.
   *
   * @param id die ID der zu löschenden Entität
   */
  void deleteById(Id id);

  /** Löscht alle Entitäten aus dem Repository. */
  void deleteAll();

  /**
   * Gibt die Anzahl der gespeicherten Entitäten zurück.
   *
   * @return die Anzahl der Entitäten
   */
  int count();

  /**
   * Persistiert alle Entitäten in den konfigurierten Speicher.
   *
   * @throws IOException falls beim Schreiben ein Fehler auftritt
   */
  void persist() throws IOException;

  /**
   * Lädt alle Entitäten aus dem konfigurierten Speicher.
   *
   * @throws IOException            falls beim Lesen ein Fehler auftritt
   * @throws ClassNotFoundException falls eine gespeicherte Klasse nicht gefunden wird
   */
  void initialize() throws IOException, ClassNotFoundException;

}
