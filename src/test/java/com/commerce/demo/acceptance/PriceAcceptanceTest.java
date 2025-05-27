package com.commerce.demo.acceptance;

import com.commerce.demo.domain.product.Category;
import com.commerce.demo.infrastructure.brand.BrandJpaEntity;
import com.commerce.demo.infrastructure.brand.BrandJpaRepository;
import com.commerce.demo.infrastructure.product.ProductJpaEntity;
import com.commerce.demo.infrastructure.product.ProductJpaRepository;
import com.commerce.demo.acceptance.AcceptanceTest;
import com.commerce.demo.acceptance.DatabaseCleanup;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PriceAcceptanceTest extends AcceptanceTest {

  @Autowired
  DatabaseCleanup cleanup;

  @Autowired
  BrandJpaRepository brandRepository;

  @Autowired
  ProductJpaRepository productRepository;

  @BeforeEach
  void setUpData() {
    브랜드별_상품_데이터_셋업();
  }

  @AfterEach
  void cleanUp() {
    cleanup.execute();
  }

  @Test
  @DisplayName("카테고리별 최저가격 브랜드와 가격, 총액을 조회한다")
  void findLowestPriceByCategory() {
    RestAssured.given()
        .when()
        .get("/api/v1/prices/lowest-by-category")
        .then()
        .statusCode(200)
        .body("items", hasSize(8))
        .body("items[0].category", equalTo("상의"))
        .body("items[0].brandName", equalTo("C"))
        .body("items[0].price", equalTo(10000))
        .body("total", equalTo(34100));
  }

  @Test
  @DisplayName("단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리별 상품가격, 총액을 조회한다")
  void findLowestTotalPriceBrand() {
    RestAssured.given()
        .when()
        .get("/api/v1/prices/lowest-by-brand")
        .then()
        .statusCode(200)
        .body("최저가.브랜드", equalTo("D"))
        .body("최저가.카테고리", hasSize(8))
        .body("총액", equalTo("36,100"));
  }

  @Test
  @DisplayName("특정 카테고리의 최저가, 최고가 브랜드와 상품 가격을 조회한다")
  void findCategoryPriceRange() {
    RestAssured.given()
        .when()
        .get("/api/v1/prices/category/상의/price-range")
        .then()
        .statusCode(200)
        .body("카테고리", equalTo("상의"))
        .body("최저가", hasSize(1))
        .body("최저가[0].브랜드", equalTo("C"))
        .body("최저가[0].가격", equalTo("10,000"))
        .body("최고가", hasSize(1))
        .body("최고가[0].브랜드", equalTo("I"))
        .body("최고가[0].가격", equalTo("11,400"));
  }

  @Test
  @DisplayName("다른 카테고리(바지)의 최저가, 최고가 브랜드와 상품 가격을 조회한다")
  void findCategoryPriceRangeForPants() {
    RestAssured.given()
        .when()
        .get("/api/v1/prices/category/바지/price-range")
        .then()
        .statusCode(200)
        .body("카테고리", equalTo("바지"))
        .body("최저가", hasSize(1))
        .body("최저가[0].브랜드", equalTo("D"))
        .body("최저가[0].가격", equalTo("3,000"))
        .body("최고가", hasSize(1))
        .body("최고가[0].브랜드", equalTo("A"))
        .body("최고가[0].가격", equalTo("4,200"));
  }

  @Test
  @DisplayName("존재하지 않는 카테고리 조회 시 예외가 발생한다")
  void findCategoryPriceRangeWithInvalidCategory() {
    RestAssured.given()
        .when()
        .get("/api/v1/prices/category/존재하지않는카테고리/price-range")
        .then()
        .statusCode(400);
  }

  private void 브랜드별_상품_데이터_셋업() {
    Map<String, Long> brandIds = setupBrands();
    setupProducts(brandIds);
  }

  private Map<String, Long> setupBrands() {
    List<BrandJpaEntity> brands = List.of(
        new BrandJpaEntity(null, "A"),
        new BrandJpaEntity(null, "B"),
        new BrandJpaEntity(null, "C"),
        new BrandJpaEntity(null, "D"),
        new BrandJpaEntity(null, "E"),
        new BrandJpaEntity(null, "F"),
        new BrandJpaEntity(null, "G"),
        new BrandJpaEntity(null, "H"),
        new BrandJpaEntity(null, "I")
    );
    List<BrandJpaEntity> savedBrands = brandRepository.saveAll(brands);
    return savedBrands.stream()
        .collect(Collectors.toMap(BrandJpaEntity::getName, BrandJpaEntity::getId));
  }

  private void setupProducts(Map<String, Long> brandIds) {
    List<ProductData> productDataList = List.of(
        new ProductData(Category.TOPS, Map.of(
            "A", 11200, "B", 10500, "C", 10000, "D", 10100,
            "E", 10700, "F", 11200, "G", 10500, "H", 10800, "I", 11400)),
        new ProductData(Category.OUTERWEAR, Map.of(
            "A", 5500, "B", 5900, "C", 6200, "D", 5100,
            "E", 5000, "F", 7200, "G", 5800, "H", 6300, "I", 6700)),
        new ProductData(Category.PANTS, Map.of(
            "A", 4200, "B", 3800, "C", 3300, "D", 3000,
            "E", 3800, "F", 4000, "G", 3900, "H", 3700, "I", 3500)),
        new ProductData(Category.SNEAKERS, Map.of(
            "A", 9000, "B", 9100, "C", 9200, "D", 9500,
            "E", 9900, "F", 9300, "G", 9000, "H", 9700, "I", 9500)),
        new ProductData(Category.BAGS, Map.of(
            "A", 2000, "B", 2100, "C", 2200, "D", 2500,
            "E", 2300, "F", 2100, "G", 2200, "H", 2100, "I", 2400)),
        new ProductData(Category.HATS, Map.of(
            "A", 1700, "B", 2000, "C", 1900, "D", 1500,
            "E", 1800, "F", 1600, "G", 1700, "H", 1600, "I", 1700)),
        new ProductData(Category.SOCKS, Map.of(
            "A", 1800, "B", 2000, "C", 2200, "D", 2400,
            "E", 2100, "F", 2300, "G", 2100, "H", 2000, "I", 1700)),
        new ProductData(Category.ACCESSORIES, Map.of(
            "A", 2300, "B", 2200, "C", 2100, "D", 2000,
            "E", 2100, "F", 1900, "G", 2000, "H", 2000, "I", 2400))
    );

    List<ProductJpaEntity> productEntities = productDataList.stream()
        .flatMap(data -> data.prices.entrySet().stream()
            .map(entry -> new ProductJpaEntity(
                null,
                data.category,
                data.category.getDisplayName() + "상품",
                entry.getValue().longValue(),
                brandIds.get(entry.getKey())
            )))
        .toList();

    productRepository.saveAll(productEntities);
  }

  private record ProductData(Category category, Map<String, Integer> prices) {

  }
}
