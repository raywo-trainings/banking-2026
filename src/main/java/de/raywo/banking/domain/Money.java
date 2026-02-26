package de.raywo.banking.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Currency;
import java.util.Objects;

public record Money(
    BigDecimal amount,
    Currency currency
) {

  public Money {
    Objects.requireNonNull(amount);
    Objects.requireNonNull(currency);
    amount = amount.setScale(2, RoundingMode.HALF_UP);
  }


  public static Money euroOf(BigDecimal amount) {
    return new Money(amount, Currency.getInstance("EUR"));
  }


  public static Money zeroEuro() {
    return euroOf(BigDecimal.ZERO);
  }


  public Money add(Money other) {
    Objects.requireNonNull(other);
    requireCurrency(other, this.currency());

    return new Money(amount.add(other.amount), currency);
  }


  public Money subtract(Money other) {
    Objects.requireNonNull(other);
    requireCurrency(other, this.currency());

    return new Money(amount.subtract(other.amount), currency);
  }


  public static Comparator<Money> sameCurrencyComparator(Currency expected) {
    Objects.requireNonNull(expected);

    return (a, b) -> {
      requireCurrency(a, expected);
      requireCurrency(b, expected);

      return a.amount.compareTo(b.amount);
    };
  }


  @Override
  public String toString() {
    NumberFormat df = NumberFormat.getCurrencyInstance();
    df.setCurrency(currency());

    return df.format(amount);
  }


  private static void requireCurrency(Money m, Currency expected) {
    if (!m.currency.equals(expected)) {
      throw new CurrencyMismatchException("Unerwartete Währung: " + m.currency
          + " (erwartet: " + expected + ")");
    }
  }

}
