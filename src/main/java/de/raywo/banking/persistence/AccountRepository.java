package de.raywo.banking.persistence;

import de.raywo.banking.domain.Account;

/**
 * Konkretes Repository für {@link Account}-Objekte mit IBAN ({@link String})
 * als Schlüssel.
 *
 * <p>Diese Klasse erbt die komplette Implementierung von
 * {@link AbstractMapBasedRepository} und legt lediglich die Typparameter fest:
 * {@code String} für die ID (IBAN) und {@code Account} für die Entität.
 * Es muss kein weiterer Code geschrieben werden – ein Beispiel dafür, wie
 * abstrakte Basisklassen Code-Duplizierung vermeiden.</p>
 *
 * @see CustomerRepository
 * @see AbstractMapBasedRepository
 */
public class AccountRepository extends AbstractMapBasedRepository<String, Account> {

  /**
   * @param storage die Speicherstrategie für Kontodaten (z.&nbsp;B. {@link FileStorage})
   */
  public AccountRepository(Storage<String, Account> storage) {
    super(storage);
  }

}
