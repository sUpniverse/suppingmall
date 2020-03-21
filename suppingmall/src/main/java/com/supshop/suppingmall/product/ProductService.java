package com.supshop.suppingmall.product;

import com.supshop.suppingmall.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    public List<Product> retrieveAllProduct() {
        return productMapper.selectAllProduct();
    }

    public Product findProduct(Long id) {
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

    @Transactional
    public void updateProduct(Long id, Product product) {
        productMapper.updateProduct(id, product);
    }

    @Transactional
    public void updateProductOption(List<ProductOption> productOptionList) {
        productMapper.updateProductOption(productOptionList);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productMapper.deleteProduct(id);
    }
}
