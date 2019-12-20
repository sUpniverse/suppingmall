package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.product.Product;

import java.util.List;

public interface ProductMapper {

    List<Product> selectAllProduct();

    Product selectProduct(Long id);

    void insertProduct(Product product);

    void updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
