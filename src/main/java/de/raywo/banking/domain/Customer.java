package de.raywo.banking.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Customer implements Serializable, Identifiable<UUID> {

  private final UUID id;
  private String name;
  private String city;


  public Customer(String name, String city) {
    Objects.requireNonNull(name, "name darf nicht null sein");
    Objects.requireNonNull(city, "city darf nicht null sein");

    this.id = UUID.randomUUID();
    this.name = name;
    this.city = city;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    Objects.requireNonNull(name, "name darf nicht null sein");
    this.name = name;
  }


  public String getCity() {
    return city;
  }


  public void setCity(String city) {
    Objects.requireNonNull(city, "city darf nicht null sein");
    this.city = city;
  }


  @Override
  public UUID getId() {
    return id;
  }


  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Customer customer = (Customer) o;
    return id.equals(customer.id);
  }


  @Override
  public int hashCode() {
    return id.hashCode();
  }


  @Override
  public String toString() {
    String idChunk = id.toString().substring(0, 8);

    return "[" + idChunk + "]: " +
        name +
        " (" + city + ")";
  }

}
