package com.commerce.demo.infrastructure.brand;

import com.commerce.demo.domain.brand.Brand;
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
class BrandRepositoryImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    private BrandRepositoryImpl brandRepository;

    @BeforeEach
    void setUp() {
        brandRepository = new BrandRepositoryImpl(brandJpaRepository);
    }

    @Test
    @DisplayName("브랜드를 저장하고 ID로 조회할 수 있다")
    void saveAndFindById() {
        // given
        Brand brand = Brand.create("Nike");

        // when
        Brand saved = brandRepository.save(brand);
        Optional<Brand> found = brandRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Nike");
    }

    @Test
    @DisplayName("모든 브랜드를 조회할 수 있다")
    void findAll() {
        // given
        Brand brand1 = Brand.create("Nike");
        Brand brand2 = Brand.create("Adidas");
        
        brandRepository.save(brand1);
        brandRepository.save(brand2);

        // when
        List<Brand> brands = brandRepository.findAll();

        // then
        assertThat(brands).hasSize(2);
        assertThat(brands).extracting("name")
                .containsExactlyInAnyOrder("Nike", "Adidas");
    }

    @Test
    @DisplayName("브랜드를 삭제할 수 있다")
    void deleteById() {
        // given
        Brand brand = Brand.create("Nike");
        Brand saved = brandRepository.save(brand);

        // when
        brandRepository.deleteById(saved.getId());

        // then
        Optional<Brand> found = brandRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회시 빈 Optional을 반환한다")
    void findByIdNotFound() {
        // when
        Optional<Brand> found = brandRepository.findById(999L);

        // then
        assertThat(found).isEmpty();
    }
} 