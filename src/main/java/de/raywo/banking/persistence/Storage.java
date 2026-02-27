package de.raywo.banking.persistence;

import java.io.IOException;
import java.util.Map;

public interface Storage<Id, T> {

  void saveAll(Map<Id, T> entity) throws IOException;

  Map<Id, T> readAll() throws IOException, ClassNotFoundException;

}
