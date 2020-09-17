package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.page.ProductCriteria;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductDetail;
import com.supshop.suppingmall.product.ProductOption;

import java.util.List;

public interface ProductMapper {

    //셀러 판매 물품 총 갯수 조회
    int findSaleProductCount(String type,Long id);

    // 모든 제품 조회
    List<Product> findAll();

    // 조건에 해당하는 모든 제품 조회
    List<Product> findAll(Long id, String category, String name, Product.ProductStatus status);

    // 판매자가 등록한 모든 상품 조회
    List<Product> findAllBySellerId(Long sellerId,ProductCriteria criteria);

    // Paging을 위한 개수 조회
    List<Product> findProductsCount();

    // 판매순별로 물춤조회
    List<Product> findAllByOrderCount();

    Product findOne(Long id);
    Product findOne(Long id, String category, String name, Product.ProductStatus status);

    int insertProduct(Product product);
    void addProductOptions(List<ProductOption> options);
    void addProductDetail(ProductDetail detail);

    void updateProduct(Long productId, Product product);

    void updateProductOption(List<ProductOption> productOptionList);

    void deleteProduct(Long id);

}
