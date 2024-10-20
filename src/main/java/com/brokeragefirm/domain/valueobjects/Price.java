package com.brokeragefirm.domain.valueobjects;

import com.brokeragefirm.common.constants.ErrorCode;
import com.brokeragefirm.domain.enums.Currency;
import com.brokeragefirm.domain.exceptions.OrderException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public record Price(BigDecimal amount, Currency currency) implements Serializable {

  public Price {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new OrderException(ErrorCode.PRICE_MUST_BE_ABOVE_ZERO);
    }

    if (currency != Currency.TRY) {
      throw new OrderException(ErrorCode.CURRENCY_NOT_SUPPORTED);
    }

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Price price = (Price) o;
    return amount.compareTo(price.amount) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount);
  }
}

