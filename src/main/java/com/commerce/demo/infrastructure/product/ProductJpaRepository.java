package com.commerce.demo.infrastructure.product;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

}
