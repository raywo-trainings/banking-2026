package de.raywo.banking.persistence;

import de.raywo.banking.domain.Customer;

import java.io.IOException;
import java.util.*;

public class CustomerRepository implements Repository<UUID, Customer> {

  private Storage<UUID, Customer> storage;


  public CustomerRepository(Storage<UUID, Customer> storage) {
    this.storage = storage;
  }


  private Map<UUID, Customer> customers = new HashMap<>();


  public void save(Customer customer) {
    customers.put(customer.getId(), customer);
  }


  public Optional<Customer> findById(UUID id) {
    return Optional.ofNullable(customers.get(id));
  }


  public Collection<Customer> findAll() {
    return customers.values();
  }


  public void delete(Customer customer) {
    customers.remove(customer.getId());
  }


  public void deleteAll() {
    customers.clear();
  }


  public int count() {
    return customers.size();
  }


  @Override
  public void persist() throws IOException {
    storage.saveAll(customers);
  }


  @Override
  public void initialize() throws IOException, ClassNotFoundException {
    this.customers = storage.readAll();
  }

}
