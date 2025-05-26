package com.commerce.demo.domain.product;

import com.commerce.demo.domain.exception.InvalidValueException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Money {

  private final long value;

  public Money(long value) {
    if (value < 0) {
      throw new InvalidValueException("가격은 음수일 수 없습니다");
    }
    this.value = value;
  }

  @Override
  public String toString() {
    return String.format("%,d", value);
  }
}
