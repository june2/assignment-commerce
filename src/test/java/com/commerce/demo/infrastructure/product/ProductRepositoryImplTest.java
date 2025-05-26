package com.commerce.demo.infrastructure.product;

import com.commerce.demo.domain.brand.Brand;
import com.commerce.demo.domain.product.Money;
import com.commerce.demo.domain.product.Product;
import com.commerce.demo.infrastructure.brand.BrandJpaEntity;
import com.commerce.demo.infrastructure.brand.BrandJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.sql.init.mode=never",
    "spring.jpa.defer-datasource-initialization=false"
})
class ProductRepositoryImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    private ProductRepositoryImpl productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepositoryImpl(productJpaRepository);
    }

    @Test
    @DisplayName("상품을 저장하고 ID로 조회할 수 있다")
    void saveAndFindById() {
        // given
        BrandJpaEntity brandEntity = new BrandJpaEntity(null, "Nike");
        brandJpaRepository.save(brandEntity);
        
        Brand brand = Brand.of(brandEntity.getId(), "Nike");
        Product product = Product.create("상의", "티셔츠", new Money(10000), brand);
        
        // when
        Product saved = productRepository.save(product);
        Optional<Product> found = productRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        Product foundProduct = found.get();
        assertThat(foundProduct.getName()).isEqualTo("티셔츠");
        assertThat(foundProduct.getCategory()).isEqualTo("상의");
        assertThat(foundProduct.getPrice().getValue()).isEqualTo(10000);
        assertThat(foundProduct.getBrand()).isNotNull();
        assertThat(foundProduct.getBrand().getId()).isEqualTo(brandEntity.getId());
        // 브랜드 이름은 repository 레이어에서 로드되지 않으므로 검증하지 않음
    }

    @Test
    @DisplayName("모든 상품을 조회할 수 있다")
    void findAll() {
        // given
        BrandJpaEntity brandEntity = new BrandJpaEntity(null, "Nike");
        brandJpaRepository.save(brandEntity);
        
        Brand brand = Brand.of(brandEntity.getId(), "Nike");
        Product product1 = Product.create("상의", "티셔츠", new Money(10000), brand);
        Product product2 = Product.create("하의", "바지", new Money(20000), brand);
        
        productRepository.save(product1);
        productRepository.save(product2);

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(2);
        assertThat(products).extracting("name")
                .containsExactlyInAnyOrder("티셔츠", "바지");
        assertThat(products).allSatisfy(product -> {
            assertThat(product.getBrand()).isNotNull();
            assertThat(product.getBrand().getId()).isEqualTo(brandEntity.getId());
            // 브랜드 이름은 repository 레이어에서 로드되지 않으므로 검증하지 않음
        });
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다")
    void deleteById() {
        // given
        BrandJpaEntity brandEntity = new BrandJpaEntity(null, "Nike");
        brandJpaRepository.save(brandEntity);
        
        Brand brand = Brand.of(brandEntity.getId(), "Nike");
        Product product = Product.create("상의", "티셔츠", new Money(10000), brand);
        Product saved = productRepository.save(product);

        // when
        productRepository.deleteById(saved.getId());

        // then
        Optional<Product> found = productRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회시 빈 Optional을 반환한다")
    void findByIdNotFound() {
        // when
        Optional<Product> found = productRepository.findById(999L);

        // then
        assertThat(found).isEmpty();
    }
} 
