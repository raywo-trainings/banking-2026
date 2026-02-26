package de.raywo.banking.persistence;

import de.raywo.banking.domain.Customer;

import java.util.*;

public class CustomerRepository implements Repository<UUID, Customer> {

  private final Map<UUID, Customer> customers = new HashMap<>();


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

}
