package com.commerce.demo.infrastructure.price;

import com.commerce.demo.domain.price.ProductWithBrand;
import com.commerce.demo.domain.price.ProductWithBrandRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductWithBrandRepositoryImpl implements ProductWithBrandRepository {

  private final EntityManager entityManager;

  @Override
  public List<ProductWithBrand> findLowestPriceProductsByCategory() {
    String sql = """
            WITH ranked_products AS (
              SELECT p.category, p.brand_id, p.price, b.name as brand_name,
                     ROW_NUMBER() OVER (PARTITION BY p.category ORDER BY p.price ASC) as rn
              FROM product p
              INNER JOIN brand b ON p.brand_id = b.id
            )
            SELECT category, brand_name, price
            FROM ranked_products
            WHERE rn = 1
            ORDER BY price DESC
            """;

    Query query = entityManager.createNativeQuery(sql);
    List<Object[]> results = query.getResultList();

    return results.stream()
        .map(row -> new ProductWithBrand(
            null,                    // id
            (String) row[0],         // category
            null,                    // name
            ((Number) row[2]).longValue(),  // price
            null,                    // brand_id
            (String) row[1]          // brand_name
        ))
        .collect(Collectors.toList());
  }

  @Override
  public List<ProductWithBrand> findLowestTotalPriceBrandProducts() {
    String sql = """
        WITH brand_totals AS (
          SELECT brand_id, SUM(price) as total_price, COUNT(DISTINCT category) as category_count
          FROM product
          GROUP BY brand_id
          HAVING category_count = (SELECT COUNT(DISTINCT category) FROM product)
          ORDER BY total_price ASC
          LIMIT 1
        )
        SELECT p.id, p.category, p.name, p.price, p.brand_id, b.name as brand_name
        FROM product p
        INNER JOIN brand b ON p.brand_id = b.id
        INNER JOIN brand_totals bt ON p.brand_id = bt.brand_id
        ORDER BY p.category
        """;

    Query query = entityManager.createNativeQuery(sql);
    List<Object[]> results = query.getResultList();

    return results.stream()
        .map(row -> new ProductWithBrand(
            ((Number) row[0]).longValue(),
            (String) row[1],
            (String) row[2],
            ((Number) row[3]).longValue(),
            ((Number) row[4]).longValue(),
            (String) row[5]
        ))
        .collect(Collectors.toList());
  }

  @Override
  public List<ProductWithBrand> findLowestPriceProductsInCategory(String category) {
    String sql = """
        SELECT p.id, p.category, p.name, p.price, p.brand_id, b.name as brand_name
        FROM product p
        INNER JOIN brand b ON p.brand_id = b.id
        WHERE p.category = :category 
        AND p.price = (SELECT MIN(price) FROM product WHERE category = :category)
        ORDER BY b.name
        """;

    Query query = entityManager.createNativeQuery(sql)
        .setParameter("category", category);
    List<Object[]> results = query.getResultList();

    return results.stream()
        .map(row -> new ProductWithBrand(
            ((Number) row[0]).longValue(),
            (String) row[1],
            (String) row[2],
            ((Number) row[3]).longValue(),
            ((Number) row[4]).longValue(),
            (String) row[5]
        ))
        .collect(Collectors.toList());
  }

  @Override
  public List<ProductWithBrand> findHighestPriceProductsInCategory(String category) {
    String sql = """
        SELECT p.id, p.category, p.name, p.price, p.brand_id, b.name as brand_name
        FROM product p
        INNER JOIN brand b ON p.brand_id = b.id
        WHERE p.category = :category 
        AND p.price = (SELECT MAX(price) FROM product WHERE category = :category)
        ORDER BY b.name
        """;

    Query query = entityManager.createNativeQuery(sql)
        .setParameter("category", category);
    List<Object[]> results = query.getResultList();

    return results.stream()
        .map(row -> new ProductWithBrand(
            ((Number) row[0]).longValue(),
            (String) row[1],
            (String) row[2],
            ((Number) row[3]).longValue(),
            ((Number) row[4]).longValue(),
            (String) row[5]
        ))
        .collect(Collectors.toList());
  }
} 
