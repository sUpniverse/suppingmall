package com.supshop.suppingmall.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    ProductMapper mapper;

    public Product retrieveProduct(String id) {
      return mapper.selectPrdoduct(id);
    }

    public void createProduct(Product product) {
        mapper.createProduct(product);
    }

    public void updateProduct(String id, Product product) {
        mapper.updateProduct(id, product);
    }

    public void deleteProduct(String id) {
        mapper.deleteProduct(id);
    }
}
