package com.supshop.suppingmall.product;

import com.supshop.suppingmall.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public List<Product> retrieveAllProduct() {
        return productMapper.selectAllProduct();
    }

    public Product retrieveProduct(Long id) {
      return productMapper.selectProduct(id);
    }

    public void createProduct(Product product) {
        productMapper.insertProduct(product);
    }

    public void updateProduct(Long id, Product product) {
        productMapper.updateProduct(id, product);
    }

    public void deleteProduct(Long id) {
        productMapper.deleteProduct(id);
    }
}
