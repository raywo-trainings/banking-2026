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


  @Override
  public Optional<T> findById(Id id) {
    return Optional.ofNullable(entityMap.get(id));
  }


  @Override
  public void save(T entity) {
    entityMap.put(entity.getId(), entity);
  }


  @Override
  public Collection<T> findAll() {
    return entityMap.values();
  }


  @Override
  public void delete(T entity) {
    entityMap.remove(entity.getId());
  }


  @Override
  public void deleteById(Id id) {
    entityMap.remove(id);
  }


  @Override
  public void deleteAll() {
    entityMap.clear();
  }


  @Override
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
