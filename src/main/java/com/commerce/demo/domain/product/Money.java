package com.commerce.demo.domain.product;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Money {

  private final long value;

  public Money(long value) {
    if (value < 0) {
      throw new IllegalArgumentException("음수 불가");
    }
    this.value = value;
  }

  @Override
  public String toString() {
    return String.format("%,d", value);
  }
}
