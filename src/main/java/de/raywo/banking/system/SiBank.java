package de.raywo.banking.system;

import de.raywo.banking.domain.Account;
import de.raywo.banking.domain.Customer;

import java.util.ArrayList;
import java.util.List;

public class SiBank {

  private String name;
  private String city;
  private final String bic;
  private final List<Account> accounts;
  private final List<Customer> customers;


  public SiBank(String name, String city, String bic) {
    this.name = name;
    this.city = city;
    this.bic = bic;
    this.accounts = new ArrayList<>();
    this.customers = new ArrayList<>();
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


  public String getBic() {
    return bic;
  }


  public List<Account> getAccounts() {
    return accounts;
  }


  public List<Customer> getCustomers() {
    return customers;
  }


  public void addAccount(Account account) {
    accounts.add(account);
  }


  public void addCustomer(Customer customer) {
    customers.add(customer);
  }


  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    SiBank siBank = (SiBank) o;
    return bic.equals(siBank.bic);
  }


  @Override
  public int hashCode() {
    return bic.hashCode();
  }


  @Override
  public String toString() {
    return name + " (" + city + ")" +
        ", BIC: " + bic;
  }
}
