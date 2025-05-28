# 커머스 API 요약

**🔗 [프론트엔드 구현 화면 확인하기](https://drive.google.com/file/d/12Or9nECkyaCwqPLYEB7cOuGi4dn5Tte_/view?usp=sharing)**

## 주요 기능
- **카테고리별 최저가 브랜드 조회**  
  `GET /api/v1/prices/lowest-by-category`
- **단일 브랜드 최저가 조회**  
  `GET /api/v1/prices/lowest-by-brand`
- **카테고리별 최저/최고가 브랜드 조회**  
  `GET /api/v1/prices/category/{category}/price-range`
- **브랜드/상품 관리 (CRUD)**  
  `POST/GET/PUT/DELETE /api/v1/brands`  
  `POST/GET/PUT/DELETE /api/v1/products`

## 기술 스택
- Spring Boot 3.2.3, Java 17
- H2 (In-Memory), Spring Data JPA
- Gradle, JUnit5, RestAssured, Mockito

## 실행 방법
```bash
git clone <repository-url>
cd assignment-commerce
./gradlew bootRun
```

## 테스트 실행
```bash
./gradlew test
```

## 아키텍처
- Web: Controller, DTO
- Application: Service, UseCase
- Domain: VO, Domain Logic
- Infrastructure: Repository, JPA

## 설계 원칙
- 도메인 중심 설계(Money, Product, Brand 등)
- 계층별 책임 분리

## 참고 문서
- [패키지 구조](README_PACKAGE_STRUCTURE.md)
- [코드 스타일](README_CODE_STYLE.md)
- [예외 처리](README_EXCEPTION_HANDLING.md)
- [도메인/JPA Entity 분리](README_DOMAIN_JPA_SEPARATION.md)
