package com.commerce.demo.infrastructure.price;

import com.commerce.demo.domain.price.ProductWithBrand;
import com.commerce.demo.domain.price.ProductWithBrandRepository;
import com.commerce.demo.domain.product.Category;
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
        WITH category_min_prices AS (
          SELECT category, MIN(price) as min_price
          FROM product
          GROUP BY category
        ),
        category_lowest_brand AS (
          SELECT p.category, MAX(b.name) as first_brand_name
          FROM product p
          INNER JOIN brand b ON p.brand_id = b.id
          INNER JOIN category_min_prices cmp ON p.category = cmp.category AND p.price = cmp.min_price
          GROUP BY p.category
        )
        SELECT p.id, p.category, p.name, p.price, p.brand_id, b.name as brand_name
        FROM product p
        INNER JOIN brand b ON p.brand_id = b.id
        INNER JOIN category_min_prices cmp ON p.category = cmp.category AND p.price = cmp.min_price
        INNER JOIN category_lowest_brand clb ON p.category = clb.category AND b.name = clb.first_brand_name
        ORDER BY p.category
        """;

    Query query = entityManager.createNativeQuery(sql);
    List<Object[]> results = query.getResultList();

    return results.stream()
        .map(row -> new ProductWithBrand(
            ((Number) row[0]).longValue(),
            Category.valueOf((String) row[1]),
            (String) row[2],
            ((Number) row[3]).longValue(),
            ((Number) row[4]).longValue(),
            (String) row[5]
        ))
        .collect(Collectors.toList());
  }

  @Override
  public List<ProductWithBrand> findLowestTotalPriceBrandProducts() {
    String sql = """
        WITH brand_category_min AS (
          SELECT brand_id, category, MIN(price) as min_price
          FROM product
          GROUP BY brand_id, category
        ),
        brand_totals AS (
          SELECT brand_id, SUM(min_price) as total_price
          FROM brand_category_min
          WHERE brand_id IN (
            SELECT brand_id
            FROM brand_category_min
            GROUP BY brand_id
            HAVING COUNT(DISTINCT category) = (SELECT COUNT(DISTINCT category) FROM product)
          )
          GROUP BY brand_id
        ),
        min_total AS (
          SELECT MIN(total_price) as min_total_price
          FROM brand_totals
        )
        SELECT p.id, p.category, p.name, p.price, p.brand_id, b.name as brand_name
        FROM product p
        INNER JOIN brand b ON p.brand_id = b.id
        INNER JOIN brand_totals bt ON p.brand_id = bt.brand_id
        INNER JOIN min_total mt ON bt.total_price = mt.min_total_price
        WHERE (p.brand_id, p.category, p.price) IN (
          SELECT brand_id, category, MIN(price)
          FROM product
          GROUP BY brand_id, category
        )
        ORDER BY p.category, p.name
        """;

    Query query = entityManager.createNativeQuery(sql);
    List<Object[]> results = query.getResultList();

    return results.stream()
        .map(row -> new ProductWithBrand(
            ((Number) row[0]).longValue(),
            Category.valueOf((String) row[1]),
            (String) row[2],
            ((Number) row[3]).longValue(),
            ((Number) row[4]).longValue(),
            (String) row[5]
        ))
        .collect(Collectors.toList());
  }

  @Override
  public List<ProductWithBrand> findLowestPriceProductsInCategory(Category category) {
    String sql = """
        SELECT p.id, p.category, p.name, p.price, p.brand_id, b.name as brand_name
        FROM product p
        INNER JOIN brand b ON p.brand_id = b.id
        WHERE p.category = :category 
        AND p.price = (SELECT MIN(price) FROM product WHERE category = :category)
        ORDER BY b.name
        """;

    Query query = entityManager.createNativeQuery(sql)
        .setParameter("category", category.name());
    List<Object[]> results = query.getResultList();

    return results.stream()
        .map(row -> new ProductWithBrand(
            ((Number) row[0]).longValue(),
            Category.valueOf((String) row[1]),
            (String) row[2],
            ((Number) row[3]).longValue(),
            ((Number) row[4]).longValue(),
            (String) row[5]
        ))
        .collect(Collectors.toList());
  }

  @Override
  public List<ProductWithBrand> findHighestPriceProductsInCategory(Category category) {
    String sql = """
        SELECT p.id, p.category, p.name, p.price, p.brand_id, b.name as brand_name
        FROM product p
        INNER JOIN brand b ON p.brand_id = b.id
        WHERE p.category = :category 
        AND p.price = (SELECT MAX(price) FROM product WHERE category = :category)
        ORDER BY b.name
        """;

    Query query = entityManager.createNativeQuery(sql)
        .setParameter("category", category.name());
    List<Object[]> results = query.getResultList();

    return results.stream()
        .map(row -> new ProductWithBrand(
            ((Number) row[0]).longValue(),
            Category.valueOf((String) row[1]),
            (String) row[2],
            ((Number) row[3]).longValue(),
            ((Number) row[4]).longValue(),
            (String) row[5]
        ))
        .collect(Collectors.toList());
  }
} 
