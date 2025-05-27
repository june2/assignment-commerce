# 커머스 API

## 🎯 기능 구현 사항

### 1️⃣ 카테고리별 최저가격 브랜드 조회
- **API**: `GET /api/v1/prices/lowest-by-category`
- **기능**: 각 카테고리에서 최저가격 브랜드와 상품 가격, 총액 조회
- **응답 예시**:
```json
{
  "items": [
    {"category": "상의", "brandName": "C", "price": 10000},
    {"category": "아우터", "brandName": "E", "price": 5000}
  ],
  "total": 34100
}
```

### 2️⃣ 단일 브랜드 최저가격 조회
- **API**: `GET /api/v1/prices/lowest-by-brand`
- **기능**: 단일 브랜드로 모든 카테고리 상품 구매 시 최저가격 브랜드 조회
- **응답 예시**:
```json
{
  "최저가": {
    "브랜드": "D",
    "카테고리": [
      {"카테고리": "상의", "가격": "10,100"},
      {"카테고리": "아우터", "가격": "5,100"}
    ]
  },
  "총액": "36,100"
}
```

### 3️⃣ 카테고리별 최저/최고가 브랜드 조회
- **API**: `GET /api/v1/prices/category/{category}/price-range`
- **기능**: 특정 카테고리의 최저가, 최고가 브랜드와 상품 가격 조회
- **응답 예시**:
```json
{
  "카테고리": "상의",
  "최저가": [{"브랜드": "C", "가격": "10,000"}],
  "최고가": [{"브랜드": "I", "가격": "11,400"}]
}
```

### 4️⃣ 브랜드 및 상품 관리 API
- **브랜드 CRUD**: `POST/GET/PUT/DELETE /api/v1/brands`
- **상품 CRUD**: `POST/GET/PUT/DELETE /api/v1/products`
- **기능**: 브랜드와 상품의 추가, 수정, 삭제 기능

## 🏗️ 기술 스택

- **Framework**: Spring Boot 3.2.3
- **Language**: Java 17
- **Database**: H2 (In-Memory)
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle
- **Test**: JUnit 5, RestAssured, Mockito

## 🚀 실행 방법

### 1. 프로젝트 클론
```bash
git clone <repository-url>
cd assignment-commerce
```

### 2. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 3. API 테스트
```bash
# 카테고리별 최저가격 조회
curl http://localhost:8080/api/v1/prices/lowest-by-category

# 단일 브랜드 최저가격 조회
curl http://localhost:8080/api/v1/prices/lowest-by-brand

# 카테고리별 가격 범위 조회
curl http://localhost:8080/api/v1/prices/category/상의/price-range
```

### 4. 테스트 실행
```bash
./gradlew test
```

## 🎨 아키텍처 설계

### 헥사고날 아키텍처 (Ports & Adapters)
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Layer     │    │ Application     │    │ Infrastructure  │
│   (Controllers) │───▶│   (Services)    │───▶│   (Repositories)│
│   (DTOs)        │    │   (Use Cases)   │    │   (JPA Entities)│
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                       ┌─────────────────┐
                       │  Domain Layer   │
                       │   (Entities)    │
                       │ (Value Objects) │
                       │  (Repositories) │
                       └─────────────────┘
```

### 핵심 설계 원칙

#### 1. **도메인 중심 설계**
- `Money` 값 객체로 가격 도메인 규칙 캡슐화
- `Product`, `Brand` 엔티티로 비즈니스 로직 응집
- 도메인 계층이 외부 의존성 없이 순수한 비즈니스 로직 담당

#### 2. **복잡한 쿼리 최적화**
```java
// 카테고리별 최저가 조회 - 윈도우 함수 활용
WITH ranked_products AS (
  SELECT category, brand_id, price, brand_name,
         ROW_NUMBER() OVER (PARTITION BY category ORDER BY price ASC) as rn
  FROM product p INNER JOIN brand b ON p.brand_id = b.id
)
SELECT category, brand_name, price FROM ranked_products WHERE rn = 1
```

#### 3. **계층별 책임 분리**
- **Web**: HTTP 요청/응답 처리, DTO 변환
- **Application**: 유스케이스 조합, 트랜잭션 관리
- **Domain**: 핵심 비즈니스 로직, 도메인 규칙
- **Infrastructure**: 데이터 접근, 외부 시스템 연동

## 🔧 주요 구현 특징

### 1. **값 객체 활용**
```java
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
}
```
- 가격 도메인 규칙을 값 객체로 캡슐화
- 불변성 보장 및 유효성 검증

### 2. **정적 팩토리 메서드**
```java
public class Product {
    // 새로운 상품 생성
    public static Product create(String category, String name, Money price, Brand brand) {
        return new Product(null, category, name, price, brand);
    }
    
    // 기존 상품 재구성
    public static Product of(Long id, String category, String name, Money price, Brand brand) {
        return new Product(id, category, name, price, brand);
    }
}
```
- 생성 의도를 명확히 표현
- 객체 생성 로직 캡슐화

### 3. **카테고리 정렬 서비스**
```java
@Service
public class CategorySortService {
    private static final List<String> CATEGORY_ORDER = List.of(
        "상의", "아우터", "바지", "스니커즈", "가방", "모자", "양말", "액세서리"
    );
    
    public List<ProductWithBrand> sortByCategory(List<ProductWithBrand> products) {
        return products.stream()
            .sorted(Comparator.comparing(product -> getCategoryOrder(product.getCategory())))
            .toList();
    }
}
```
- 비즈니스 요구사항에 맞는 카테고리 순서 보장
- 응답 데이터의 일관성 확보

## 📚 상세 문서

프로젝트의 상세한 구현 내용은 다음 문서를 참조하세요:

- **[📦 패키지 구조](README_PACKAGE_STRUCTURE.md)** - 헥사고날 아키텍처 기반 패키지 설계
- **[🎨 코드 스타일](README_CODE_STYLE.md)** - DTO 변환 규칙 및 네이밍 컨벤션
- **[⚠️ 예외 처리](README_EXCEPTION_HANDLING.md)** - 도메인별 예외 처리 전략

## 🧪 테스트 전략

### 1. **단위 테스트**
- Domain 계층: 순수 Java 객체 테스트
- Application 계층: Mock을 활용한 서비스 로직 테스트

### 2. **통합 테스트**
- Repository 계층: `@DataJpaTest`로 데이터 접근 로직 검증
- API 계층: `@SpringBootTest`로 전체 플로우 검증

### 3. **인수 테스트**
- RestAssured를 활용한 End-to-End 테스트
- 실제 HTTP 요청/응답 시나리오 검증

## 🎯 과제 요구사항 달성

✅ **구현 1**: 카테고리별 최저가격 브랜드와 상품 가격, 총액 조회  
✅ **구현 2**: 단일 브랜드로 모든 카테고리 상품 구매 시 최저가격 브랜드 조회  
✅ **구현 3**: 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격 조회  
✅ **구현 4**: 브랜드 및 상품 추가/업데이트/삭제 API  

✅ **기술 요구사항**:
- Spring Boot 기반 REST API
- H2 인메모리 데이터베이스 사용
- 단위/통합 테스트 작성
- 프론트엔드 페이지 구현

## 📄 라이선스

이 프로젝트는 과제 목적으로 작성되었습니다. 