package de.raywo.banking.persistence;

import de.raywo.banking.domain.Customer;

import java.util.UUID;

/**
 * Konkretes Repository für {@link Customer}-Objekte mit {@link UUID} als Schlüssel.
 *
 * <p>Diese Klasse erbt die komplette Implementierung von
 * {@link AbstractMapBasedRepository} und legt lediglich die Typparameter fest:
 * {@code UUID} für die ID und {@code Customer} für die Entität.
 * Es muss kein weiterer Code geschrieben werden – ein Beispiel dafür, wie
 * abstrakte Basisklassen Code-Duplizierung vermeiden.</p>
 *
 * @see AccountRepository
 * @see AbstractMapBasedRepository
 */
public class CustomerRepository extends AbstractMapBasedRepository<UUID, Customer> {

  /**
   * @param storage die Speicherstrategie für Kundendaten (z.&nbsp;B. {@link FileStorage})
   */
  public CustomerRepository(Storage<UUID, Customer> storage) {
    super(storage);
  }

}
