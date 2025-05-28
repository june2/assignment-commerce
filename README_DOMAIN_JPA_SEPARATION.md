# 도메인 엔티티와 JPA 엔티티 분리 이유

도메인 엔티티는 비즈니스 규칙과 상태를 표현하는 순수 객체(POJO)이고, JPA 엔티티는 데이터베이스 테이블과 매핑되는 영속성 객체입니다.

## 구현 예시 

### 도메인 엔티티 예시
**파일명:** `src/main/java/com/commerce/demo/domain/brand/Brand.java`
```java
public class Brand {
  private final Long id;
  private final String name;
  // ... (생성자, 팩토리 메서드 등)
}
```

### JPA 엔티티 예시
**파일명:** `src/main/java/com/commerce/demo/infrastructure/brand/BrandJpaEntity.java`
```java
@Entity
@Table(name = "brand")
public class BrandJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  // ... (JPA 어노테이션, 생성자 등)
}
``` 

## 왜 분리하는가?

1. **순수 도메인 모델 보장**
   - 도메인 엔티티는 비즈니스 규칙과 로직에만 집중하고, JPA나 DB와 같은 외부 기술에 의존하지 않습니다.
   - 테스트가 쉽고, 도메인 로직의 변경이 인프라(저장 방식)와 분리됩니다.

2. **관심사 분리(Separation of Concerns)**
   - JPA 엔티티는 DB 매핑, 영속성 관리 등 인프라스트럭처 역할에 집중합니다.
   - 도메인 엔티티는 비즈니스 상태와 행위만을 표현합니다.

3. **유연한 아키텍처**
   - DB, ORM, 저장 방식이 바뀌어도 도메인 계층은 영향받지 않습니다.
   - 도메인 객체를 다양한 저장소(예: JPA, NoSQL, 외부 API)와 쉽게 연결할 수 있습니다.

4. **테스트 용이성**
   - 도메인 객체는 순수 Java 객체(POJO)로, JPA 환경 없이도 단위 테스트가 가능합니다.

## 결론
- 도메인과 인프라 계층의 명확한 분리는 유지보수성과 확장성을 높이고, 비즈니스 로직의 순수성을 보장합니다.
