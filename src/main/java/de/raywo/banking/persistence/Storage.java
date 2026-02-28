package de.raywo.banking.persistence;

import java.io.IOException;
import java.util.Map;

/**
 * Interface, das den Speichermechanismus abstrahiert.
 *
 * <p>{@code Storage} definiert den Vertrag zum Lesen und Schreiben einer Map
 * von Entitäten. Die konkrete Implementierung entscheidet, <em>wohin</em>
 * die Daten geschrieben werden – z.&nbsp;B. in eine Datei ({@link FileStorage}),
 * eine Datenbank oder einen anderen Speicher.</p>
 *
 * <p>Diese Trennung entspricht dem
 * <a href="https://en.wikipedia.org/wiki/Strategy_pattern">Strategy-Pattern</a>:
 * Das {@link AbstractMapBasedRepository} delegiert die eigentliche Persistierung
 * an das {@code Storage}-Interface. Dadurch bleibt das Repository unabhängig vom
 * konkreten Speichermechanismus, und die Speicherstrategie kann ausgetauscht werden,
 * ohne den Rest der Anwendung zu berühren.</p>
 *
 * <p><b>Konzepte:</b> Interfaces, Generics, Strategy-Pattern,
 * Schichtenarchitektur</p>
 *
 * @param <Id> der Typ des Identifiers
 * @param <T>  der Typ der gespeicherten Entität
 * @see FileStorage
 * @see AbstractMapBasedRepository
 */
public interface Storage<Id, T> {

  /**
   * Schreibt alle Entitäten in den Speicher.
   *
   * @param entity die zu speichernde Map (ID → Entität)
   * @throws IOException falls beim Schreiben ein Fehler auftritt
   */
  void saveAll(Map<Id, T> entity) throws IOException;

  /**
   * Liest alle Entitäten aus dem Speicher.
   *
   * @return eine Map aller gespeicherten Entitäten (ID → Entität)
   * @throws IOException            falls beim Lesen ein Fehler auftritt
   * @throws ClassNotFoundException falls eine gespeicherte Klasse nicht gefunden wird
   */
  Map<Id, T> readAll() throws IOException, ClassNotFoundException;

}
