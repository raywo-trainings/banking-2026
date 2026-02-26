package de.raywo.banking.persistence;

import java.util.Collection;
import java.util.Optional;

public interface Repository<Id, T> {

  void save(T entity);

  Optional<T> findById(Id id);

  Collection<T> findAll();

  void delete(T entity);

  void deleteAll();

  int count();

}
