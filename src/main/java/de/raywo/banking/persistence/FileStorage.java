package de.raywo.banking.persistence;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Konkrete Implementierung des {@link Storage}-Interfaces, die Daten als
 * Binärdatei über Java-Serialisierung persistiert.
 *
 * <p>Nutzt {@link ObjectOutputStream} und {@link ObjectInputStream}, um die
 * gesamte Map aller Objekte in eine Datei zu schreiben bzw. daraus zu lesen.
 * Damit das funktioniert, müssen alle gespeicherten Objekte (und deren Felder)
 * das Marker-Interface {@link java.io.Serializable} implementieren.</p>
 *
 * <p>Beide Methoden verwenden <b>Try-with-Resources</b> (seit Java 7): Die in
 * der {@code try}-Klammer deklarierten Ressourcen werden am Ende des Blocks
 * automatisch geschlossen – auch wenn eine Exception auftritt. Die Ressourcen
 * müssen dafür {@link AutoCloseable} implementieren, was bei allen
 * {@code OutputStream}/{@code InputStream}-Klassen der Fall ist.</p>
 *
 * <p><b>Konzepte:</b> Serialisierung/Deserialisierung, Try-with-Resources,
 * Interfaces ({@code Storage}), Pattern Matching bei {@code instanceof}
 * (in {@link #readAll()})</p>
 *
 * @param <Id> der Typ des Identifiers
 * @param <T>  der Typ der gespeicherten Entität
 * @see Storage
 */
public class FileStorage<Id, T> implements Storage<Id, T> {

  private final String path;


  /**
   * Erzeugt einen neuen FileStorage mit dem angegebenen Dateipfad.
   *
   * @param path der Pfad zur Binärdatei (falls {@code null}, wird ein
   *             Standardpfad verwendet)
   */
  public FileStorage(String path) {
    this.path = Objects.requireNonNullElse(path, "default-file-storage.bin");
  }


  /**
   * Serialisiert die gesamte Map in eine Binärdatei.
   *
   * <p>Verwendet <b>Try-with-Resources</b>: Die Streams werden automatisch
   * geschlossen, auch wenn eine Exception auftritt.</p>
   *
   * @param collection die zu speichernde Map (ID → Entität)
   * @throws IOException falls beim Schreiben ein Fehler auftritt
   */
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


  /**
   * Deserialisiert die Map aus der Binärdatei. Falls die Datei nicht existiert
   * oder leer ist, wird eine leere Map zurückgegeben.
   *
   * <p>Verwendet <b>Try-with-Resources</b> und <b>Pattern Matching bei instanceof</b>
   * (seit Java 16): {@code if (obj instanceof Map<?, ?> map)} prüft den Typ und
   * bindet das Ergebnis direkt an die Variable {@code map}, ohne einen separaten
   * Cast zu benötigen.</p>
   *
   * @return die geladene Map, oder eine leere Map falls keine Daten vorhanden sind
   * @throws IOException            falls beim Lesen ein Fehler auftritt
   * @throws ClassNotFoundException falls eine gespeicherte Klasse nicht gefunden wird
   */
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
