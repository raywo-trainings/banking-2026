package de.raywo.banking.persistence;

import de.raywo.banking.domain.Identifiable;

import java.io.IOException;
import java.util.*;

/**
 * Abstrakte Basisimplementierung des {@link Repository}-Interfaces auf Basis
 * einer {@link HashMap}.
 *
 * <p>Diese Klasse demonstriert, wie abstrakte Klassen als <b>Basisimplementierung</b>
 * eingesetzt werden: Sie implementiert alle Methoden des {@code Repository}-Interfaces
 * vollständig, sodass konkrete Repositories wie {@link AccountRepository} und
 * {@link CustomerRepository} lediglich die Typparameter festlegen müssen – ohne
 * {@code save}, {@code findById}, {@code findAll} usw. identisch zu implementieren.
 * Sollte sich die Speicherlogik ändern, muss nur diese eine Stelle angepasst werden.</p>
 *
 * <p>Die eigentliche Persistierung wird an das {@link Storage}-Interface delegiert
 * (Strategy-Pattern). Dadurch bleibt das Repository unabhängig davon, ob die Daten
 * in eine Datei, eine Datenbank oder einen anderen Speicher geschrieben werden.</p>
 *
 * <p>Die In-Memory-Speicherung nutzt eine {@link HashMap} ({@code entityMap}),
 * die Schlüssel-Wert-Paare (ID → Entität) speichert und schnellen Zugriff über
 * die ID ermöglicht.</p>
 *
 * <p><b>Konzepte:</b> Abstrakte Klassen als Basisimplementierung, Generics
 * (mit Typeinschränkung), Collections ({@code HashMap}), Strategy-Pattern,
 * Vermeidung von Code-Duplizierung</p>
 *
 * @param <Id> der Typ des Identifiers
 * @param <T>  der Typ der gespeicherten Entität
 * @see AccountRepository
 * @see CustomerRepository
 * @see Storage
 */
public abstract class AbstractMapBasedRepository<Id, T extends Identifiable<Id>>
    implements Repository<Id, T> {

  protected final Storage<Id, T> storage;
  protected Map<Id, T> entityMap = new HashMap<>();


  /**
   * Erzeugt ein neues Repository mit der angegebenen Storage-Strategie.
   *
   * @param storage die Speicherstrategie für die Persistierung
   * @throws NullPointerException falls {@code storage} {@code null} ist
   */
  public AbstractMapBasedRepository(Storage<Id, T> storage) {
    this.storage = Objects.requireNonNull(storage, "storage must not be null");
  }


  @Override
  public Optional<T> findById(Id id) {
    return Optional.ofNullable(entityMap.get(id));
  }


  @Override
  public void save(T entity) {
    entityMap.put(entity.getId(), entity);
  }


  @Override
  public Collection<T> findAll() {
    return entityMap.values();
  }


  @Override
  public void delete(T entity) {
    entityMap.remove(entity.getId());
  }


  @Override
  public void deleteById(Id id) {
    entityMap.remove(id);
  }


  @Override
  public void deleteAll() {
    entityMap.clear();
  }


  @Override
  public int count() {
    return entityMap.size();
  }


  @Override
  public void persist() throws IOException {
    storage.saveAll(entityMap);
  }


  @Override
  public void initialize() throws IOException, ClassNotFoundException {
    this.entityMap = storage.readAll();
  }

}
