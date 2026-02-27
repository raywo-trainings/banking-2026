package de.raywo.banking.persistence;

import de.raywo.banking.domain.Identifiable;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface Repository<Id, T extends Identifiable<Id>> {

  void save(T entity);

  Optional<T> findById(Id id);

  Collection<T> findAll();

  void delete(T entity);

  void deleteById(Id id);

  void deleteAll();

  int count();

  void persist() throws IOException;

  void initialize() throws IOException, ClassNotFoundException;

}
