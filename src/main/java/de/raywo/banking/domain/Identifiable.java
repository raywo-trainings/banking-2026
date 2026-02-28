package de.raywo.banking.domain;

/**
 * Generisches Interface, das garantiert, dass eine Klasse eine eindeutige
 * Identität besitzt.
 *
 * <p>Der Typparameter {@code T} legt den Typ des Identifiers fest. So kann
 * z.&nbsp;B. {@code Customer} mit {@code Identifiable<UUID>} und {@code Account}
 * mit {@code Identifiable<String>} arbeiten, ohne dass jeweils ein eigenes
 * Interface benötigt wird.</p>
 *
 * <p><b>Konzepte:</b> Interfaces, Generics</p>
 *
 * @param <T> der Typ des Identifiers (z.&nbsp;B. {@link java.util.UUID} oder
 *            {@link String})
 * @see de.raywo.banking.persistence.Repository
 */
public interface Identifiable<T> {

  /**
   * Gibt den eindeutigen Identifier dieses Objekts zurück.
   *
   * @return den Identifier, nie {@code null}
   */
  T getId();

}
