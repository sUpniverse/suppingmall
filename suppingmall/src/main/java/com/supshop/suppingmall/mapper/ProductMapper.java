package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.ProductCriteria;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductDetail;
import com.supshop.suppingmall.product.ProductOption;

import java.util.List;

public interface ProductMapper {

    //셀러 판매 물품 총 갯수 조회
    int findSaleProductCount(Long categoryId,Long sellerId, String name, Product.ProductStatus status);

    // Paging을 위한 개수 조회
    int findAllCount(Long categoryId, String name, Product.ProductStatus status);

    // 모든 제품 조회 (product, product detail, product option까지 모두)
    // 조건에 추가
    List<Product> findAll(Long sellerId, Long categoryId, String name, Product.ProductStatus status,Criteria criteria);

    // 모든 제품 조회 (only product 정보만)
    List<Product> findAllPart();
    // 조건에 추가
    List<Product> findAllPart(Long sellerId, Long categoryId, String name, Product.ProductStatus status,  Criteria criteria);

    // 부모 카테고리 아이디로 조회
    List<Product> findAllPartByParentCategory(Long parentId, Criteria criteria);

    // 판매순별로 물품조회
    List<Product> findAllOrderByOrdersQuantity();

    Product findOne(Long id);
    Product findOne(Long id, String category, String name, Product.ProductStatus status);

    int insertProduct(Product product);
    void addProductOptions(List<ProductOption> options);
    void addProductDetail(ProductDetail detail);

    void updateProduct(Long productId, Product product);

    void updateProductOption(List<ProductOption> productOptionList);

    void deleteProduct(Long id);

}
