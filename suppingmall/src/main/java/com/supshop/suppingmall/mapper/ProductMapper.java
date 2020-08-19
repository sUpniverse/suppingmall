package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductDetail;
import com.supshop.suppingmall.product.ProductOption;

import java.util.List;

public interface ProductMapper {

    List<Product> findProducts();
    List<Product> findProductsById(Long sellerId);

    Product findProduct(Long sellerId);

    int insertProduct(Product product);
    void addProductOptions(List<ProductOption> options);
    void addProductDetail(ProductDetail detail);

    void updateProduct(Long productId, Product product);

    void updateProductOption(List<ProductOption> productOptionList);

    void deleteProduct(Long id);

}
