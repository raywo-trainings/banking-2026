package de.raywo.banking.domain;

import java.util.UUID;

public class Customer {

  private final UUID id;
  private String name;
  private String city;


  public Customer(String name, String city) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.city = city;
  }


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getCity() {
    return city;
  }


  public void setCity(String city) {
    this.city = city;
  }


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
