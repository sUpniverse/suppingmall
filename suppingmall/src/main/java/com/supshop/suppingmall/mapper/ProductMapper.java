package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductDetail;
import com.supshop.suppingmall.product.ProductOption;

import java.util.List;

public interface ProductMapper {

    List<Product> selectAllProduct();

    Product selectProduct(Long id);

    void insertProduct(Product product);
    void addProductOptions(List<ProductOption> options);
    void addProductDetail(ProductDetail detail);

    void updateProduct(Long id, Product product);

    void updateProductOption(List<ProductOption> productOptionList);

    void deleteProduct(Long id);
}
