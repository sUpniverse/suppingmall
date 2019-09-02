package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.product.Product;

import java.util.List;

public interface ProductMapper {

    List<Product> selectAllProduct();

    Product selectPrdoduct(String id);

    void insertProduct(Product product);

    void updateProduct(String id, Product product);

    void deleteProduct(String id);
}
