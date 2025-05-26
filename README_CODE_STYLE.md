# 코드 스타일 가이드

## 개요
이 문서는 프로젝트의 일관된 코드 스타일과 변환 규칙을 정의합니다.

## 1. DTO 변환 규칙

### 1.1 기본 원칙

#### `from()` 메서드 사용
- **용도**: Domain 객체 → DTO 변환
- **위치**: Response DTO 클래스 내부
- **접근제한자**: `public static`

```java
// ✅ 올바른 예시
public record ProductResponse(Long id, String name, String category, long price) {
    
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getCategory(),
            product.getPrice().getValue()
        );
    }
}
```

**이유:**
- **명확성**: 변환 방향이 명확함 (Domain → DTO)
- **일관성**: 모든 Response DTO에서 동일한 패턴 사용
- **가독성**: 메서드명만으로 변환 의도 파악 가능

### 1.2 복잡한 변환 규칙

#### 다중 파라미터 `from()` 메서드
```java
// ✅ 복잡한 변환 시 다중 파라미터 허용
public static CategoryPriceRangeResponse from(String category,
    List<ProductWithBrand> lowestProducts, 
    List<ProductWithBrand> highestProducts) {
    // 변환 로직
}
```

#### 포맷팅 로직 분리
```java
// ✅ 포맷팅 로직은 private static 메서드로 분리
public static BrandPriceResponse from(ProductWithBrand product) {
    return new BrandPriceResponse(
        product.getBrandName(),
        formatPrice(product.getPrice())  // 포맷팅 분리
    );
}

private static String formatPrice(long price) {
    return String.format("%,d", price);
}
```

**이유:**
- **재사용성**: 포맷팅 로직 중복 제거
- **테스트 용이성**: 포맷팅 로직 독립적 테스트 가능
- **유지보수성**: 포맷 변경 시 한 곳만 수정

### 1.3 Request DTO 규칙

#### 생성자 변환 사용 금지
```java
// ❌ Request DTO에는 from() 메서드 사용하지 않음
public record ProductRequest(String name, String category, long price, Long brandId) {
    // from() 메서드 없음 - Service 계층에서 직접 변환
}
```

**이유:**
- **단순성**: Request는 단순 데이터 전달 역할
- **보안**: 외부 입력 데이터의 직접적 변환 방지
- **검증**: Service 계층에서 검증과 함께 변환 처리

## 2. Domain Entity ↔ JPA Entity 변환 규칙

### 2.1 JPA Entity → Domain Entity

#### `toDomain()` 메서드 사용
```java
// ✅ JPA Entity 내부에 toDomain() 메서드 정의
@Entity
@Table(name = "product")
public class ProductJpaEntity {
    
    @Id
    private Long id;
    private String category;
    private String name;
    private long price;
    private Long brandId;
    
    public Product toDomain() {
        return new Product(
            this.id, 
            this.category, 
            this.name, 
            new Money(this.price), 
            null  // Brand는 별도 조회 필요
        );
    }
}
```

**이유:**
- **캡슐화**: JPA Entity가 자신의 변환 로직 소유
- **일관성**: 모든 JPA Entity에서 동일한 메서드명 사용
- **의존성 최소화**: Domain이 JPA에 의존하지 않음

### 2.2 Domain Entity → JPA Entity

#### Repository 구현체에서 직접 변환
```java
// ✅ Repository 구현체에서 생성자 사용
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity(
            product.getId(),
            product.getCategory(),
            product.getName(),
            product.getPrice().getValue(),
            product.getBrand() != null ? product.getBrand().getId() : null
        );
        ProductJpaEntity saved = productJpaRepository.save(entity);
        return saved.toDomain();
    }
}
```

**이유:**
- **계층 분리**: Infrastructure 계층에서 변환 책임
- **유연성**: 복잡한 변환 로직 처리 가능
- **성능**: 불필요한 메서드 호출 오버헤드 제거

### 2.3 Domain Entity의 정적 팩토리 메서드

#### 특별한 경우에만 사용
```java
// ✅ 복잡한 생성 로직이 있는 경우에만 사용
public class Brand {
    private final Long id;
    private final String name;
    
    public static Brand from(BrandJpaEntity entity) {
        // 복잡한 검증이나 변환 로직이 있는 경우에만 사용
        return new Brand(entity.getId(), entity.getName());
    }
}
```

**이유:**
- **예외적 사용**: 일반적으로는 `toDomain()` 사용 권장
- **복잡성 관리**: 복잡한 생성 로직이 있을 때만 사용
- **명확성**: 생성 의도가 명확한 경우에만 사용

## 3. 메서드 체이닝 규칙

### 3.1 Stream API 사용
```java
// ✅ 메서드 참조 사용 권장
return productRepository.findAll().stream()
    .map(ProductResponse::from)  // 메서드 참조
    .toList();

// ✅ 복잡한 변환은 람다 사용
return products.stream()
    .map(product -> CategoryLowestPriceResponse.from(product))
    .collect(Collectors.toList());
```

**이유:**
- **간결성**: 메서드 참조로 코드 간소화
- **가독성**: 변환 의도가 명확히 드러남
- **성능**: 컴파일러 최적화 가능

## 4. 네이밍 컨벤션

### 4.1 변환 메서드명
- **Domain → DTO**: `from()`
- **JPA Entity → Domain**: `toDomain()`
- **포맷팅**: `format{Type}()` (예: `formatPrice()`)

### 4.2 DTO 클래스명
- **Request**: `{Entity}Request`
- **Response**: `{Entity}Response`
- **복합 Response**: `{Purpose}Response`

```java
// ✅ 올바른 네이밍
ProductRequest           // 단순 요청
ProductResponse          // 단순 응답
CategoryLowestPriceResponse  // 목적이 명확한 복합 응답
```

## 5. 주의사항

### 5.1 순환 참조 방지
```java
// ❌ 순환 참조 위험
public static ProductResponse from(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getBrand().getName(),  // NPE 위험
        product.getPrice().getValue()
    );
}

// ✅ 안전한 변환
public static ProductResponse from(Product product) {
    return new ProductResponse(
        product.getId(),
        product.getBrand() != null ? product.getBrand().getName() : null,
        product.getPrice().getValue()
    );
}
```

### 5.2 불변성 유지
```java
// ✅ Record 사용으로 불변성 보장
public record ProductResponse(Long id, String name) {
    // 자동으로 불변 객체 생성
}
```

**이유:**
- **안전성**: 객체 상태 변경 방지
- **스레드 안전성**: 동시성 문제 해결
- **예측 가능성**: 객체 상태 변화 없음
