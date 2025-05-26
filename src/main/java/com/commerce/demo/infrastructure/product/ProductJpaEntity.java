package com.commerce.demo.infrastructure.product;

import com.commerce.demo.domain.product.Money;
import com.commerce.demo.domain.brand.Brand;
import jakarta.persistence.*;
import com.commerce.demo.domain.product.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String category;
  private String name;
  private long price;
  private Long brandId;

  public Product toDomain() {
    Brand brand = new Brand(this.brandId, null);
    return new Product(this.id, this.category, this.name, new Money(this.price), brand);
  }

  public static ProductJpaEntity from(Product product) {
    return new ProductJpaEntity(
        product.getId(),
        product.getCategory(),
        product.getName(),
        product.getPrice().getValue(),
        product.getBrand().getId()
    );
  }
} 
