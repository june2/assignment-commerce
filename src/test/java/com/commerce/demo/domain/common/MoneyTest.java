package com.commerce.demo.domain.common;

import com.commerce.demo.domain.product.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MoneyTest {

  @Test
  @DisplayName("Money는 음수로 생성할 수 없다")
  void negativeValueThrows() {
    assertThatThrownBy(() -> new Money(-1)).isInstanceOf(IllegalArgumentException.class);
  }
  
  @Test
  @DisplayName("Money의 equals/hashCode가 값 기반으로 동작한다")
  void equalsAndHashCode() {
    Money m1 = new Money(1000);
    Money m2 = new Money(1000);
    assertThat(m1).isEqualTo(m2);
    assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
  }

  @Test
  @DisplayName("Money의 toString은 천단위 콤마가 포함된다")
  void toStringFormat() {
    Money m = new Money(1234567);
    assertThat(m.toString()).isEqualTo("1,234,567");
  }
} 
