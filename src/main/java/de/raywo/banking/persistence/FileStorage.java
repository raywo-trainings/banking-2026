package de.raywo.banking.persistence;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FileStorage<Id, T> implements Storage<Id, T> {

  private final String path;


  public FileStorage(String path) {
    this.path = Objects.requireNonNullElse(path, "default-file-storage.bin");
  }


  @Deprecated
  public void saveAllOld(Map<Id, T> collection) throws IOException {
    FileOutputStream fos = new FileOutputStream(path);
    ObjectOutputStream outStream = new ObjectOutputStream(fos);
    outStream.writeObject(collection);
    outStream.close();
    fos.close();
  }


  @Override
  public void saveAll(Map<Id, T> collection) throws IOException {
    Path file = Path.of(path);

    // Optional, aber oft sinnvoll: Zielordner anlegen (falls vorhanden: no-op)
    Path parent = file.getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }

    try (var out = Files.newOutputStream(
        file,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.WRITE);
         var oos = new ObjectOutputStream(out)) {

      oos.writeObject(collection);
    }
  }


  @Deprecated
  public Map<Id, T> readAllOld() throws IOException, ClassNotFoundException {
    FileInputStream fis = new FileInputStream(path);
    ObjectInputStream inputStream = new ObjectInputStream(fis);

    final Object readObject = inputStream.readObject();

    inputStream.close();
    fis.close();

    return (Map<Id, T>) readObject;
  }


  @Override
  public Map<Id, T> readAll() throws IOException, ClassNotFoundException {
    Path file = Path.of(path);

    if (Files.notExists(file) || Files.size(file) == 0L) {
      return new HashMap<>();
    }

    try (var in = Files.newInputStream(file);
         var ois = new ObjectInputStream(in)) {

      Object obj = ois.readObject();
      if (obj instanceof Map<?, ?> map) {
        @SuppressWarnings("unchecked")
        Map<Id, T> typed = (Map<Id, T>) map;
        return typed;
      }

      throw new IOException("Unerwarteter Inhalt in Datei " + file + ": " + obj.getClass().getName());
    } catch (EOFException | StreamCorruptedException e) {
      return new HashMap<>();
    }
  }
}
