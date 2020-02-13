package com.supshop.suppingmall.product;

import com.supshop.suppingmall.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void createProduct(Product product) {
        productMapper.insertProduct(product);
        Long productId = product.getProductId();
        List<ProductOption> options = product.getOptions();
        for(ProductOption option : options) {
            option.setProductId(productId);
        }
        productMapper.addProductOptions(options);
        ProductDetail detail = product.getDetail();
        detail.setProductId(productId);
        productMapper.addProductDetail(detail);

    }

    public void updateProduct(Long id, Product product) {
        productMapper.updateProduct(id, product);
    }

    public void deleteProduct(Long id) {
        productMapper.deleteProduct(id);
    }
}
