package de.raywo.banking.persistence;

import de.raywo.banking.domain.Account;

public class AccountRepository extends AbstractMapBasedRepository<String, Account> {

  public AccountRepository(Storage<String, Account> storage) {
    super(storage);
  }

}
