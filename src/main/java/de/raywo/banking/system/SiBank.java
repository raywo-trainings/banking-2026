package de.raywo.banking.system;

import de.raywo.banking.domain.Account;
import de.raywo.banking.domain.Customer;
import de.raywo.banking.persistence.AccountRepository;
import de.raywo.banking.persistence.CustomerRepository;
import de.raywo.banking.persistence.FileStorage;
import de.raywo.banking.persistence.Repository;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public class SiBank {

  private String name;
  private String city;
  private final String bic;
  private final Repository<String, Account> accountRepository;
  private final Repository<UUID, Customer> customerRepository;

  private static SiBank instance;

  public static SiBank getInstance(String name, String city, String bic) {
    if (instance == null) {
      instance = new SiBank(name, city, bic);
    }
    return instance;
  }


  private SiBank(String name, String city, String bic) {
    this.name = name;
    this.city = city;
    this.bic = bic;
    this.accountRepository = new AccountRepository(new FileStorage<>("accounts.bin"));
    this.customerRepository = new CustomerRepository(new FileStorage<>("customers.bin"));

    try {
      this.accountRepository.initialize();
      this.customerRepository.initialize();
    } catch (IOException | ClassNotFoundException e) {
      // Do nothing because no file existed
      System.out.println("Keine Datendateien gefunden.");
    }
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


  public Collection<Customer> getCustomers() {
    return customerRepository.findAll();
  }


  public void addAccount(Account account) {
    accountRepository.save(account);
  }


  public Account getAccount(String iban) throws NotFoundException {
    return accountRepository
        .findById(iban)
        .orElseThrow(() -> new NotFoundException("Ein Konto mit der IBAN " + iban + " existiert nicht"));
  }


  public void addCustomer(Customer customer) {
    customerRepository.save(customer);
  }


  public void persist() throws IOException {
    accountRepository.persist();
    customerRepository.persist();
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
