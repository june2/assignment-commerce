package com.commerce.demo.domain.price;

import com.commerce.demo.domain.product.Category;
import java.util.List;

public interface ProductWithBrandRepository {

  List<ProductWithBrand> findLowestPriceProductsByCategory();

  List<ProductWithBrand> findLowestTotalPriceBrandProducts();

  List<ProductWithBrand> findLowestPriceProductsInCategory(Category category);

  List<ProductWithBrand> findHighestPriceProductsInCategory(Category category);
}
