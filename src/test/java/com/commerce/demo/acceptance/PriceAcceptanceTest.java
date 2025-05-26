package com.commerce.demo.acceptance;

import com.commerce.demo.infrastructure.brand.BrandJpaEntity;
import com.commerce.demo.infrastructure.brand.BrandJpaRepository;
import com.commerce.demo.infrastructure.product.ProductJpaEntity;
import com.commerce.demo.infrastructure.product.ProductJpaRepository;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;


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
    var response = RestAssured.given()
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/api/v1/prices/lowest-by-category")
        .then().log().all()
        .statusCode(200)
        .extract();

    var body = response.response().getBody();

    assertThat(body.jsonPath().getLong("total")).isEqualTo(34100L);
  }

  @Test
  @DisplayName("단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리별 상품가격, 총액을 조회한다")
  void findLowestTotalPriceBrand() {
    var response = RestAssured.given()
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/api/v1/prices/lowest-by-brand")
        .then().log().all()
        .statusCode(200)
        .extract();

    var body = response.response().getBody();

    assertThat(body.jsonPath().getString("총액")).isEqualTo("36,100");
  }

  @Test
  @DisplayName("특정 카테고리의 최저가, 최고가 브랜드와 상품 가격을 조회한다")
  void findCategoryPriceRange() {
    var response = RestAssured.given()
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/api/v1/prices/category/상의/price-range")
        .then().log().all()
        .statusCode(200)
        .extract();

    var body = response.response().getBody();

    // 응답 구조 검증
    assertThat(body.jsonPath().getString("카테고리")).isNotNull();
    assertThat(body.jsonPath().getList("최저가")).isNotEmpty();
    assertThat(body.jsonPath().getList("최고가")).isNotEmpty();

    // 카테고리명 검증
    assertThat(body.jsonPath().getString("카테고리")).isEqualTo("상의");

    // 최저가 브랜드와 가격 검증 (상의 카테고리에서 C브랜드가 10,000원으로 최저가)
    List<Map<String, String>> 최저가_리스트 = body.jsonPath().getList("최저가");
    assertThat(최저가_리스트).hasSize(1);

    Map<String, String> 최저가_정보 = 최저가_리스트.get(0);
    assertThat(최저가_정보.get("브랜드")).isEqualTo("C");
    assertThat(최저가_정보.get("가격")).isEqualTo("10,000");

    // 최고가 브랜드와 가격 검증 (상의 카테고리에서 I브랜드가 11,400원으로 최고가)
    List<Map<String, String>> 최고가_리스트 = body.jsonPath().getList("최고가");
    assertThat(최고가_리스트).hasSize(1);

    Map<String, String> 최고가_정보 = 최고가_리스트.get(0);
    assertThat(최고가_정보.get("브랜드")).isEqualTo("I");
    assertThat(최고가_정보.get("가격")).isEqualTo("11,400");
  }

  @Test
  @DisplayName("다른 카테고리(바지)의 최저가, 최고가 브랜드와 상품 가격을 조회한다")
  void findCategoryPriceRangeForPants() {
    var response = RestAssured.given()
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/api/v1/prices/category/바지/price-range")
        .then().log().all()
        .statusCode(200)
        .extract();

    var body = response.response().getBody();

    // 카테고리명 검증
    assertThat(body.jsonPath().getString("카테고리")).isEqualTo("바지");

    // 최저가 브랜드와 가격 검증 (바지 카테고리에서 D브랜드가 3,000원으로 최저가)
    List<Map<String, String>> 최저가_리스트 = body.jsonPath().getList("최저가");
    assertThat(최저가_리스트).hasSize(1);

    Map<String, String> 최저가_정보 = 최저가_리스트.get(0);
    assertThat(최저가_정보.get("브랜드")).isEqualTo("D");
    assertThat(최저가_정보.get("가격")).isEqualTo("3,000");

    // 최고가 브랜드와 가격 검증 (바지 카테고리에서 A브랜드가 4,200원으로 최고가)
    List<Map<String, String>> 최고가_리스트 = body.jsonPath().getList("최고가");
    assertThat(최고가_리스트).hasSize(1);

    Map<String, String> 최고가_정보 = 최고가_리스트.get(0);
    assertThat(최고가_정보.get("브랜드")).isEqualTo("A");
    assertThat(최고가_정보.get("가격")).isEqualTo("4,200");
  }

  @Test
  @DisplayName("존재하지 않는 카테고리 조회 시 예외가 발생한다")
  void findCategoryPriceRangeWithInvalidCategory() {
    RestAssured.given()
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/api/v1/prices/category/존재하지않는카테고리/price-range")
        .then().log().all()
        .statusCode(400);
  }

  private void 브랜드별_상품_데이터_셋업() {
    Map<String, Long> brandIds = setupBrands();
    setupProducts(brandIds);
  }

  private Map<String, Long> setupBrands() {
    List<BrandJpaEntity> brandEntities = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I")
        .stream()
        .map(name -> new BrandJpaEntity(null, name))
        .toList();

    brandEntities = brandRepository.saveAll(brandEntities);

    return brandEntities.stream()
        .collect(HashMap::new, (map, entity) -> map.put(entity.getName(), entity.getId()),
            HashMap::putAll);
  }

  private void setupProducts(Map<String, Long> brandIds) {
    List<ProductData> productDataList = List.of(
        new ProductData("상의", Map.of(
            "A", 11200, "B", 10500, "C", 10000, "D", 10100,
            "E", 10700, "F", 11200, "G", 10500, "H", 10800, "I", 11400)),
        new ProductData("아우터", Map.of(
            "A", 5500, "B", 5900, "C", 6200, "D", 5100,
            "E", 5000, "F", 7200, "G", 5800, "H", 6300, "I", 6700)),
        new ProductData("바지", Map.of(
            "A", 4200, "B", 3800, "C", 3300, "D", 3000,
            "E", 3800, "F", 4000, "G", 3900, "H", 3100, "I", 3200)),
        new ProductData("스니커즈", Map.of(
            "A", 9000, "B", 9100, "C", 9200, "D", 9500,
            "E", 9900, "F", 9300, "G", 9000, "H", 9700, "I", 9500)),
        new ProductData("가방", Map.of(
            "A", 2000, "B", 2100, "C", 2200, "D", 2500,
            "E", 2300, "F", 2100, "G", 2200, "H", 2100, "I", 2400)),
        new ProductData("모자", Map.of(
            "A", 1700, "B", 2000, "C", 1900, "D", 1500,
            "E", 1800, "F", 1600, "G", 1700, "H", 1600, "I", 1700)),
        new ProductData("양말", Map.of(
            "A", 1800, "B", 2000, "C", 2200, "D", 2400,
            "E", 2100, "F", 2300, "G", 2100, "H", 2000, "I", 1700)),
        new ProductData("액세서리", Map.of(
            "A", 2300, "B", 2200, "C", 2100, "D", 2000,
            "E", 2100, "F", 1900, "G", 2000, "H", 2000, "I", 2400))
    );

    List<ProductJpaEntity> productEntities = productDataList.stream()
        .flatMap(data -> data.prices.entrySet().stream()
            .map(entry -> new ProductJpaEntity(
                null,
                data.category,
                data.category + "상품",
                entry.getValue().longValue(),
                brandIds.get(entry.getKey())
            )))
        .toList();

    productRepository.saveAll(productEntities);
  }

  private record ProductData(String category, Map<String, Integer> prices) {

  }
}
