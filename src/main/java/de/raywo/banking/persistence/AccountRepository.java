package de.raywo.banking.persistence;

import de.raywo.banking.domain.Account;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountRepository implements Repository<String, Account> {

  private final Storage<String, Account> storage;

  private Map<String, Account> accounts = new HashMap<>();


  public AccountRepository(Storage<String, Account> storage) {
    this.storage = storage;
  }


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


  @Override
  public void persist() throws IOException {
    storage.saveAll(accounts);
  }


  @Override
  public void initialize() throws IOException, ClassNotFoundException {
    this.accounts = storage.readAll();
  }

}
