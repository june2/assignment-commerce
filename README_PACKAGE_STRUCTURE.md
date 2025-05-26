# 패키지 구조 설명

## 전체 패키지 구조

```
src/main/java/com/commerce/demo/
├── Application.java                    # 스프링 부트 메인 클래스
├── domain/                            # 도메인 계층 (핵심 비즈니스 로직)
│   ├── brand/                         # 브랜드 도메인
│   ├── price/                         # 가격 도메인
│   └── product/                       # 상품 도메인
├── application/                       # 애플리케이션 계층 (유스케이스)
│   ├── BrandService.java
│   ├── ProductService.java
│   └── ProductWithBrandService.java
├── infrastructure/                    # 인프라스트럭처 계층 (외부 연동)
│   ├── brand/                         # 브랜드 데이터 접근
│   ├── price/                         # 가격 데이터 접근
│   └── product/                       # 상품 데이터 접근
└── web/                              # 웹 계층 (사용자 인터페이스)
    ├── dto/                          # 데이터 전송 객체
    ├── exception/                    # 예외 처리
    ├── BrandController.java
    ├── ProductController.java
    └── ProductWithBrandController.java
```

## 계층별 상세 설명

### 1. Domain 계층 (`domain/`)
**핵심 비즈니스 로직과 규칙을 담당하는 계층**

#### `domain/brand/`
- `Brand.java`: 브랜드 도메인 엔티티
- `BrandRepository.java`: 브랜드 저장소 인터페이스

#### `domain/product/`
- `Product.java`: 상품 도메인 엔티티
- `Money.java`: 가격 값 객체 (Value Object)
- `ProductRepository.java`: 상품 저장소 인터페이스

#### `domain/price/`
- `ProductWithBrand.java`: 브랜드 정보가 포함된 상품 도메인 객체
- `ProductWithBrandRepository.java`: 브랜드별 가격 조회 저장소 인터페이스

**특징:**
- 외부 의존성이 없는 순수한 비즈니스 로직
- 인터페이스를 통해 외부 계층과 소통
- 도메인 규칙과 불변성 보장

### 2. Application 계층 (`application/`)
**유스케이스를 구현하고 도메인 객체들을 조합하는 계층**

- `BrandService.java`: 브랜드 관련 비즈니스 로직
- `ProductService.java`: 상품 관련 비즈니스 로직  
- `ProductWithBrandService.java`: 브랜드별 가격 조회 비즈니스 로직

**역할:**
- 트랜잭션 관리 (`@Transactional`)
- 도메인 객체들 간의 협력 조정
- 비즈니스 유스케이스 구현

### 3. Infrastructure 계층 (`infrastructure/`)
**외부 시스템과의 연동을 담당하는 계층**

#### `infrastructure/brand/`
- `BrandJpaEntity.java`: JPA 엔티티
- `BrandJpaRepository.java`: JPA 저장소 인터페이스
- `BrandRepositoryImpl.java`: 도메인 저장소 구현체

#### `infrastructure/product/`
- `ProductJpaEntity.java`: JPA 엔티티
- `ProductJpaRepository.java`: JPA 저장소 인터페이스
- `ProductRepositoryImpl.java`: 도메인 저장소 구현체

#### `infrastructure/price/`
- `ProductWithBrandRepositoryImpl.java`: 복잡한 가격 조회 쿼리 구현

**특징:**
- 데이터베이스 접근 로직
- 도메인 객체 ↔ JPA 엔티티 변환
- 외부 API 연동 (필요시)

### 4. Web 계층 (`web/`)
**사용자 인터페이스와 HTTP 요청/응답을 처리하는 계층**

#### Controllers
- `BrandController.java`: 브랜드 REST API
- `ProductController.java`: 상품 REST API
- `ProductWithBrandController.java`: 브랜드별 가격 조회 API

#### `web/dto/`
- Request/Response DTO 클래스들
- 외부 API와 내부 도메인 객체 간의 변환

#### `web/exception/`
- 전역 예외 처리
- HTTP 상태 코드 매핑

## 아키텍처 장점

### 1. **의존성 역전 (Dependency Inversion)**
```
Web → Application → Domain ← Infrastructure
```
- Domain이 중심이 되고, 다른 계층들이 Domain에 의존
- Infrastructure는 Domain의 인터페이스를 구현

### 2. **관심사의 분리 (Separation of Concerns)**
- 각 계층은 명확한 책임을 가짐
- 비즈니스 로직이 기술적 세부사항과 분리

### 3. **테스트 용이성**
- Domain 계층은 순수 Java 객체로 단위 테스트 용이
- Mock을 통한 계층별 독립적 테스트 가능

### 4. **유연성과 확장성**
- 데이터베이스 변경 시 Infrastructure 계층만 수정
- 새로운 API 추가 시 Web 계층만 확장
