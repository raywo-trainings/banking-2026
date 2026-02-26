package de.raywo.banking.persistence;

import de.raywo.banking.domain.Account;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountRepository implements Repository<String, Account> {

  private final Map<String, Account> accounts = new HashMap<>();


  public void save(Account account) {
    accounts.put(account.getIban(), account);
  }


  public Optional<Account> findById(String id) {
    return Optional.ofNullable(accounts.get(id));
  }


  public Collection<Account> findAll() {
    return accounts.values();
  }


  public void delete(Account account) {
    accounts.remove(account.getIban());
  }


  public void deleteAll() {
    accounts.clear();
  }


  public int count() {
    return accounts.size();
  }

}
