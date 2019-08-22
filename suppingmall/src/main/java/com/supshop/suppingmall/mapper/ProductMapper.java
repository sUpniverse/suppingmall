package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.product.Product;

public interface ProductMapper {

    Product selectPrdoduct(String id);

    String insertProduct(Product product);

    String updateProduct(String id, Product product);

    String deleteProduct(String id);
}
