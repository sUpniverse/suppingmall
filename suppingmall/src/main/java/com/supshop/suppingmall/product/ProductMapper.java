package com.supshop.suppingmall.product;

public interface ProductMapper {

    Product selectPrdoduct(String id);

    String createProduct(Product product);

    String updateProduct(String id, Product product);

    String deleteProduct(String id);
}
