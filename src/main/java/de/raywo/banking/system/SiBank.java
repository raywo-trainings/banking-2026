package de.raywo.banking.system;

import de.raywo.banking.domain.Account;
import de.raywo.banking.domain.Customer;
import de.raywo.banking.persistence.AccountRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SiBank {

  private String name;
  private String city;
  private final String bic;
  private final AccountRepository accountRepository;
  private final List<Customer> customers;


  public SiBank(String name, String city, String bic) {
    this.name = name;
    this.city = city;
    this.bic = bic;
    this.accountRepository = new AccountRepository();
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


  public Collection<Account> getAccounts() {
    return accountRepository.findAll();
  }


  public List<Customer> getCustomers() {
    return customers;
  }


  public void addAccount(Account account) {
    accountRepository.save(account);
  }


  public Account getAccount(String iban) throws NotFoundException {
    return accountRepository
        .findById(iban)
        .orElseThrow(()-> new NotFoundException("Ein Konto mit der IBAN " + iban + " existiert nicht"));
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
