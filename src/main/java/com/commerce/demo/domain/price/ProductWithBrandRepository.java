package com.commerce.demo.domain.price;

import java.util.List;

public interface ProductWithBrandRepository {

  List<ProductWithBrand> findLowestPriceProductsByCategory();

  List<ProductWithBrand> findLowestTotalPriceBrandProducts();

  List<ProductWithBrand> findLowestPriceProductsInCategory(String category);

  List<ProductWithBrand> findHighestPriceProductsInCategory(String category);
}
