package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.page.ProductCriteria;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductDetail;
import com.supshop.suppingmall.product.ProductOption;

import java.util.List;

public interface ProductMapper {

    List<Product> findAll(Long id, String category, String name, Product.ProductStatus status);
    List<Product> findAll();
    List<Product> findLatestAll();

    List<Product> findAllBySellerId(Long sellerId, ProductCriteria criteria);

    List<Product> findAllByOrderCount();

    Product findOne(Long id);
    Product findOne(Long id, String category, String name, Product.ProductStatus status);

    int insertProduct(Product product);
    void addProductOptions(List<ProductOption> options);
    void addProductDetail(ProductDetail detail);

    void updateProduct(Long productId, Product product);

    void updateProductOption(List<ProductOption> productOptionList);

    void deleteProduct(Long id);

}
