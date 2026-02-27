package de.raywo.banking.persistence;

import de.raywo.banking.domain.Identifiable;

import java.io.IOException;
import java.util.*;

public abstract class AbstractMapBasedRepository<Id, T extends Identifiable<Id>>
    implements Repository<Id, T> {

  protected final Storage<Id, T> storage;
  protected Map<Id, T> entityMap = new HashMap<>();


  public AbstractMapBasedRepository(Storage<Id, T> storage) {
    this.storage = Objects.requireNonNull(storage, "storage must not be null");
  }


  public Optional<T> findById(Id id) {
    return Optional.ofNullable(entityMap.get(id));
  }


  public void save(T entity) {
    entityMap.put(entity.getId(), entity);
  }


  public Collection<T> findAll() {
    return entityMap.values();
  }


  public void delete(T entity) {
    entityMap.remove(entity.getId());
  }


  public void deleteAll() {
    entityMap.clear();
  }


  public int count() {
    return entityMap.size();
  }


  @Override
  public void persist() throws IOException {
    storage.saveAll(entityMap);
  }


  @Override
  public void initialize() throws IOException, ClassNotFoundException {
    this.entityMap = storage.readAll();
  }

}
