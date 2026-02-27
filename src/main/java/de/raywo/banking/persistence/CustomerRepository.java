package de.raywo.banking.persistence;

import de.raywo.banking.domain.Customer;

import java.util.UUID;

public class CustomerRepository extends AbstractMapBasedRepository<UUID, Customer> {

  public CustomerRepository(Storage<UUID, Customer> storage) {
    super(storage);
  }

}
